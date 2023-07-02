package com.example.projectsvt.controller;

import com.example.projectsvt.dto.user.CreateUserDto;
import com.example.projectsvt.dto.user.PasswordChangeDto;
import com.example.projectsvt.dto.user.UpdateProfilDTO;
import com.example.projectsvt.model.User;
import com.example.projectsvt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @PutMapping(path = "/change-password", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeDto passwordChangeDto){
        User user = userService.getCurrentUser();
        return userService.changePassword(passwordChangeDto, user);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody UpdateProfilDTO userDTO){
        User user = userService.getCurrentUser();
        if(user == null){
            return new ResponseEntity<>("Not found user !", HttpStatus.BAD_REQUEST);
        }
        user.setDescription(userDTO.getDescription());
        user.setDisplayName(userDTO.getDisplayName());

        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{username}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<User> findByUsername(@PathVariable String username){
        User user = userService.findByUsername(username);
        if(user != null){
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
}
