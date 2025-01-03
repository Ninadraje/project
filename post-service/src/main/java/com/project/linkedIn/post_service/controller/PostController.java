package com.project.linkedIn.post_service.controller;

import com.project.linkedIn.post_service.auth.UserContextHolder;
import com.project.linkedIn.post_service.dto.PostDto;
import com.project.linkedIn.post_service.dto.PostRequestCreateDto;
import com.project.linkedIn.post_service.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;


    //Create a Post
    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostRequestCreateDto postRequestCreateDto, HttpServletRequest httpServletRequest){

        PostDto createPost= postService.createPost(postRequestCreateDto);
        return new ResponseEntity<>(createPost, HttpStatus.CREATED);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPost(@PathVariable Long postId){

        Long userId= UserContextHolder.getCurrentUserId();


        PostDto post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/users/{userId}/allPosts")
    public ResponseEntity<List<PostDto>> getAllPostsOfUser(@PathVariable Long userId){

        List<PostDto> posts= postService.getAllPostsOfUser(userId);

        return ResponseEntity.ok(posts);
    }



}
