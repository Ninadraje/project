package com.project.linkedIn.post_service.service;


import com.project.linkedIn.event.PostLikedEvent;
import com.project.linkedIn.post_service.auth.UserContextHolder;
import com.project.linkedIn.post_service.entity.Post;
import com.project.linkedIn.post_service.entity.PostLike;
import com.project.linkedIn.post_service.exception.BadRequestException;
import com.project.linkedIn.post_service.exception.ResourceNotFoundException;
import com.project.linkedIn.post_service.repository.PostLikeRespository;
import com.project.linkedIn.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRespository postLikeRespository;

    private final PostRepository postRepository;

    private final KafkaTemplate<Long, PostLikedEvent> kafkaTemplate;

    @Value("${kafka.topic.post-liked-topic}")
    private String KAFKA_POST_LIKED_TOPIC;

    public void likePost(Long postId){

        Long userId= UserContextHolder.getCurrentUserId();

        log.info("Attempting to like the post with postId : {}",postId);

        Post post = postRepository.findById(postId).orElseThrow(
                ()-> new ResourceNotFoundException("Post not found with post id : "+postId)
        );

        boolean alreadyLiked = postLikeRespository.existsByUserIdAndPostId(userId,postId);

        if(alreadyLiked) throw  new BadRequestException("Cannot like the same post again!");

        PostLike postlike = new PostLike();
        postlike.setPostId(postId);
        postlike.setUserId(userId);
        postLikeRespository.save(postlike);
        log.info("Post with id: {} like successfully",postId);

        //Sending message to Kafka
        PostLikedEvent postLikedEvent = PostLikedEvent.newBuilder()
                .setPostId(postId)
                .setLikedByUserId(userId)
                .setCreatorId(post.getUserId())
                .build();

        kafkaTemplate.send(KAFKA_POST_LIKED_TOPIC,postId,postLikedEvent);
    }


    public void unlikePost(Long postId){

        log.info("Attempting to un-like the post with postId : {}",postId);

        Long userId= UserContextHolder.getCurrentUserId();

        boolean exists= postRepository.existsById(postId);

        if(!exists) throw new ResourceNotFoundException("Post not found with post id : "+postId );

        boolean alreadyLiked = postLikeRespository.existsByUserIdAndPostId(userId,postId);
        if(!alreadyLiked) throw  new BadRequestException("Cannot unlike the post which is not liked!");


        postLikeRespository.deleteByUserIdAndPostId(userId,postId);
        log.info("Post with id: {} unliked successfully",postId);
    }


}
