package com.example.projectsvt.service;

import com.example.projectsvt.dto.react.CreateReactPostDto;
import com.example.projectsvt.model.Enums.ReactionType;
import com.example.projectsvt.model.Post;
import com.example.projectsvt.model.React;
import com.example.projectsvt.model.User;
import com.example.projectsvt.repository.ReactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReactService {

    private final ReactionRepository reactionRepository;

    private final PostService postService;

    private final UserService userService;

    public ResponseEntity<?> createReactforPost(CreateReactPostDto createReactPostDto) {

        if(createReactPostDto.getReactedBy() == null){
            return new ResponseEntity<>("ReactedBy ID must not be empty", HttpStatus.BAD_REQUEST);
        }

        if(!createReactPostDto.getReactionType().equals("LIKE") && !createReactPostDto.getReactionType().equals("DISLIKE") && !createReactPostDto.getReactionType().equals("HEART")){
            return new ResponseEntity<>("ReactionType must be LIKE or DISLIKE or HEART", HttpStatus.BAD_REQUEST);
        }

        if(createReactPostDto.getPost() == null) {
            return new ResponseEntity<>("PostID must not be empty", HttpStatus.BAD_REQUEST);
        }

        React react = new React();

        react.setType(ReactionType.valueOf(createReactPostDto.getReactionType()));

        ResponseEntity<?> findByIdUserResponse = userService.findById(createReactPostDto.getReactedBy());
        if(findByIdUserResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdUserResponse;
        }

        User user = (User) findByIdUserResponse.getBody();

        react.setReactedBy(user);

        ResponseEntity<?> findByIdPostResponse = postService.findPostById(createReactPostDto.getPost());
        if(findByIdPostResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)){
            return findByIdPostResponse;
        }

        Post post = (Post) findByIdPostResponse.getBody();
        react.setPost(post);

        react = reactionRepository.save(react);

        String response = String.format("React saved successfully ! ReactID: %d", react.getId());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> findReactById(Long id) {
        Optional<React> optionalReact = reactionRepository.findById(id);

        if(optionalReact.isEmpty()){
            return new ResponseEntity<>("Post with given ID does not exist", HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(optionalReact.get(), HttpStatus.OK);
    }
}
