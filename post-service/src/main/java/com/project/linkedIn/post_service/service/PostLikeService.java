package com.project.linkedIn.post_service.service;


import com.project.linkedIn.post_service.entity.PostLike;
import com.project.linkedIn.post_service.exception.BadRequestException;
import com.project.linkedIn.post_service.exception.ResourceNotFoundException;
import com.project.linkedIn.post_service.repository.PostLikeRespository;
import com.project.linkedIn.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostLikeService {

    private final PostLikeRespository postLikeRespository;

    private final PostRepository postRepository;

    public void likePost(Long postId, Long userId){

        log.info("Attempting to like the post with postId : {}",postId);

        boolean exists= postRepository.existsById(postId);

        if(!exists) throw new ResourceNotFoundException("Post not found with post id : "+postId );

        boolean alreadyLiked = postLikeRespository.existsByUserIdAndPostId(userId,postId);

        if(alreadyLiked) throw  new BadRequestException("Cannot like the same post again!");

        PostLike postlike = new PostLike();
        postlike.setPostId(postId);
        postlike.setUserId(userId);
        postLikeRespository.save(postlike);
        log.info("Post with id: {} like successfully",postId);
    }


    public void unlikePost(Long postId, Long userId){

        log.info("Attempting to un-like the post with postId : {}",postId);

        boolean exists= postRepository.existsById(postId);

        if(!exists) throw new ResourceNotFoundException("Post not found with post id : "+postId );

        boolean alreadyLiked = postLikeRespository.existsByUserIdAndPostId(userId,postId);
        if(!alreadyLiked) throw  new BadRequestException("Cannot unlike the post which is not liked!");


        postLikeRespository.deleteByUserIdAndPostId(userId,postId);
        log.info("Post with id: {} unliked successfully",postId);
    }


}
