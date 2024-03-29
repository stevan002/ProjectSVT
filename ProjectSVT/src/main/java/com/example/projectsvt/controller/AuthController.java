package com.example.projectsvt.controller;

import com.example.projectsvt.dto.user.CreateUserDto;
import com.example.projectsvt.dto.user.LoginUserDto;
import com.example.projectsvt.dto.user.UserTokenState;
import com.example.projectsvt.model.User;
import com.example.projectsvt.security.TokenUtils;
import com.example.projectsvt.service.AuthService;
import com.example.projectsvt.service.UserDetailsServiceImpl;
import com.example.projectsvt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.Date;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final UserDetailsServiceImpl userDetailsService;
    private final UserService userService;

    private final TokenUtils tokenUtils;

    private final AuthenticationManager authenticationManager;

    @PostMapping(path = "/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@RequestBody CreateUserDto createUserDto){
        return authService.registerUser(createUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<UserTokenState> createAuthenticationToken(
            @RequestBody LoginUserDto authenticationRequest, HttpServletResponse response) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails;
        try {
            userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        user.setLastLogin(LocalDateTime.now());
        userService.update(user);
        String accessToken = tokenUtils.generateToken(userDetails);
        Date expiresIn = tokenUtils.getExpirationDateFromToken(accessToken);
        return new ResponseEntity<>(new UserTokenState(accessToken, expiresIn), HttpStatus.OK);
    }


}
