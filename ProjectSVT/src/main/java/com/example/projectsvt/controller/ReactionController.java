package com.example.projectsvt.controller;

import com.example.projectsvt.dto.react.CreateReactPostDto;
import com.example.projectsvt.service.ReactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reaction")
public class ReactionController {

    private final ReactService reactService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createReaction(@RequestBody CreateReactPostDto createReactPostDto){
        return reactService.createReactforPost(createReactPostDto);
    }

    @GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> findReactionById(@PathVariable("id") Long id){
        return reactService.findReactById(id);
    }

}
