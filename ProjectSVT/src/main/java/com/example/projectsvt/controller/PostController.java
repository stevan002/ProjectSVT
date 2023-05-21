package com.example.projectsvt.controller;

import com.example.projectsvt.dto.post.CreatePostDto;
import com.example.projectsvt.dto.post.UpdatePostDto;
import com.example.projectsvt.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createPost(@RequestBody CreatePostDto createPostDto){
        return postService.createPost(createPostDto);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findPostById(@PathVariable("id") Long id) {
        return postService.findPostById(id);
    }

    @GetMapping
    public ResponseEntity<?> findPostsByUser(@RequestParam(name = "user") Long userId){
        return postService.findPostsByUser(userId);
    }

    @PutMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updatePost(@PathVariable("id") Long id,
                                        @RequestBody UpdatePostDto updatePostDto) {
        return postService.updatePost(id, updatePostDto);
    }

    @DeleteMapping(path = "/{id}/{user-id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id,
                                        @PathVariable("user-id") Long userId) {
        return postService.deletePost(id, userId);
    }
}
