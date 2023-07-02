package com.example.projectsvt.service;

import com.example.projectsvt.dto.post.CreatePostDto;
import com.example.projectsvt.dto.post.UpdatePostDto;
import com.example.projectsvt.model.Group;
import com.example.projectsvt.model.Post;
import com.example.projectsvt.model.User;
import com.example.projectsvt.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final GroupService groupService;
    private final UserService userService;

    public ResponseEntity<?> createPost(User user, CreatePostDto createPostDto) {
        if(createPostDto.getContent() == null || createPostDto.getContent().isEmpty()) {
            return new ResponseEntity<>("Content must not be empty", HttpStatus.BAD_REQUEST);
        }

        Post post = new Post();

        post.setCreatedBy(user);

//        if (createPostDto.getContainedBy() != null) {
//            ResponseEntity<?> findByIdGroupResponse = groupService.findById(createPostDto.getContainedBy());
//            Group group = (Group) findByIdGroupResponse.getBody();
//            post.setContainedBy(group);
//        }

//        if (createPostDto.getImage() != null) {
//            post.setImage(createPostDto.getImage());
//        }

        post.setContent(createPostDto.getContent());
        post = postRepository.save(post);

        String response = String.format("Post saved successfully ! PostID: %d", post.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Post findPostById(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public ResponseEntity<?> findPostsByUser(Long userId){
        ResponseEntity<?> findByIdUserResponse = userService.findById(userId);
        if(findByIdUserResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdUserResponse;
        }

        List<Post> postList = postRepository.findByCreatedBy(userId);
        if(postList.isEmpty()) {
            return new ResponseEntity<>("There aren't any posts posted by given user", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    public ResponseEntity<?> updatePost(Long id, String content, User user){
        Post post = findPostById(id);
        if(post == null) {

            return new ResponseEntity<>("Post updated successfully", HttpStatus.NOT_FOUND);
        }
        post.setContent(content);
        post.setCreatedBy(user);
        postRepository.save(post);

        return new ResponseEntity<>("Post updated successfully", HttpStatus.OK);
    }

    public ResponseEntity<?> deletePost(Long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty()){
            return new ResponseEntity<>("Post with given ID does not exist", HttpStatus.NOT_FOUND);
        }

        Post post = optionalPost.get();


        postRepository.delete(post);
        return new ResponseEntity<>("Post successfully deleted !", HttpStatus.OK);

    }

    public ResponseEntity<?> getAll(){
        List<Post> allPosts = new ArrayList<>();
        allPosts = postRepository.findAll();
        return new ResponseEntity<>(allPosts, HttpStatus.OK);
    }
}
