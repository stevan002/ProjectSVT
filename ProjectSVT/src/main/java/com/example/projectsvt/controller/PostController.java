package com.example.projectsvt.controller;

import com.example.projectsvt.dto.post.CreatePostDto;
import com.example.projectsvt.dto.post.UpdatePostDto;
import com.example.projectsvt.model.Comment;
import com.example.projectsvt.model.Post;
import com.example.projectsvt.model.User;
import com.example.projectsvt.service.CommentService;
import com.example.projectsvt.service.PostService;
import com.example.projectsvt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.activation.CommandInfo;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/post")
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostDto createPostDto){
        User user = userService.getCurrentUser();
        ResponseEntity<?> post = postService.createPost(user, createPostDto);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping(path = {"/all"}, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Post>> getAll(){
        return (ResponseEntity<List<Post>>) postService.getAll();
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public Post findPostById(@PathVariable("id") Long id) {
        return postService.findPostById(id);
    }

    @GetMapping
    public ResponseEntity<?> findPostsByUser(@RequestParam(name = "user") Long userId){
        return postService.findPostsByUser(userId);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<?> updatePost(@PathVariable Long id,
                                        @RequestBody String content) {
        User user = userService.getCurrentUser();
        postService.updatePost(id, content, user);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{postId}/comments")
    public ResponseEntity<Comment> addCommentToPost(@PathVariable Long postId, @RequestBody Comment comment){
        User user = userService.getCurrentUser();
        Comment newComment = commentService.addCommentToPost(postId, comment, user);
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<Comment>> findCommentsForPost(@PathVariable Long postId){
        List<Comment> comments = commentService.findCommentsForPost(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @RequestBody Comment updatedComment){
        Comment comment = commentService.updateComment(commentId, updatedComment);
        if(comment == null){
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping(path = "/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        return commentService.deleteComment(commentId);
    }


}
