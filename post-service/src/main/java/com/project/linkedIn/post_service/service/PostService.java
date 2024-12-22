package com.project.linkedIn.post_service.service;


import com.project.linkedIn.post_service.dto.PostDto;
import com.project.linkedIn.post_service.dto.PostRequestCreateDto;
import com.project.linkedIn.post_service.entity.Post;
import com.project.linkedIn.post_service.exception.ResourceNotFoundException;
import com.project.linkedIn.post_service.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;

    private final ModelMapper modelMapper;

    public PostDto createPost(PostRequestCreateDto postRequestCreateDto, long userId) {

        Post post = modelMapper.map(postRequestCreateDto, Post.class);

        post.setUserId(userId);

        Post savedPost= postRepository.save(post);

        //This will convert Post class to PostDto class
        return modelMapper.map(savedPost, PostDto.class);


    }

    public PostDto getPostById(Long postId) {

         log.debug("Retrieving post with Id : {}",postId);

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
