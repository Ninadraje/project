package com.project.linkedIn.post_service.service;


import com.project.linkedIn.event.PostCreatedEvent;
import com.project.linkedIn.post_service.auth.UserContextHolder;
import com.project.linkedIn.post_service.clients.ConnectionsClient;
import com.project.linkedIn.post_service.dto.PersonDto;
import com.project.linkedIn.post_service.dto.PostDto;
import com.project.linkedIn.post_service.dto.PostRequestCreateDto;
import com.project.linkedIn.post_service.entity.Post;
import com.project.linkedIn.post_service.exception.ResourceNotFoundException;
import com.project.linkedIn.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    private final ConnectionsClient connectionsClient;

    private final KafkaTemplate<Long, PostCreatedEvent> kafkaTemplate;

    @Value("${kafka.topic.post-created-topic}")
    private String KAFKA_POST_CREATED_TOPIC;




    public PostDto createPost(PostRequestCreateDto postRequestCreateDto) {

        Post post = modelMapper.map(postRequestCreateDto, Post.class);

        Long userId= UserContextHolder.getCurrentUserId();

        post.setUserId(userId);

        Post savedPost= postRepository.save(post);

        //Sending message to Kafka
        PostCreatedEvent postCreatedEvent= modelMapper.map(postRequestCreateDto,PostCreatedEvent.class);
        postCreatedEvent.setPostId(savedPost.getId());
        postCreatedEvent.setCreatorId(userId);

        kafkaTemplate.send(KAFKA_POST_CREATED_TOPIC,postCreatedEvent);

        //This will convert Post class to PostDto class
        return modelMapper.map(savedPost, PostDto.class);


    }

    public PostDto getPostById(Long postId) {

         log.debug("Retrieving post with Id : {}",postId);

         Long userId = UserContextHolder.getCurrentUserId();

         List<PersonDto> firstConnections =connectionsClient.getFirstConnections(userId);

//        TODO send Notification to all connections

         Post post =postRepository.findById(postId).orElseThrow(() ->
                    new ResourceNotFoundException("Post not found with id : "+postId));

         return modelMapper.map(post,PostDto.class);

    }

    public List<PostDto> getAllPostsOfUser(Long userId) {
        List<Post> posts=postRepository.findByUserId(userId);

        return posts.stream().
                map((element) -> modelMapper.map(element, PostDto.class))
                .collect(Collectors.toList());
    }
}
