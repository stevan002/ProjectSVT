package com.example.projectsvt.service;

import com.example.projectsvt.dto.user.CreateUserDto;
import com.example.projectsvt.dto.user.LoginUserDto;
import com.example.projectsvt.model.User;
import com.example.projectsvt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ResponseEntity<?> registerUser(CreateUserDto createUserDto) {

        if (createUserDto.getPassword() == null || createUserDto.getPassword().isEmpty()) {
            return new ResponseEntity<>("Password must not be empty", HttpStatus.BAD_REQUEST);
        }
        if (createUserDto.getEmail() == null || createUserDto.getEmail().isEmpty()) {
            return new ResponseEntity<>("Email must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (createUserDto.getFirstName() == null || createUserDto.getFirstName().isEmpty()) {
            return new ResponseEntity<>("First name must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (createUserDto.getLastName() == null || createUserDto.getLastName().isEmpty()) {
            return new ResponseEntity<>("Last name must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (createUserDto.getRole() == null) {
            return new ResponseEntity<>("Role must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (createUserDto.getPassword().length() < 6) {
            return new ResponseEntity<>("Password must be at least 6 characters long",HttpStatus.BAD_REQUEST);
        }

        if ( userRepository.existsByEmailOrUsername(createUserDto.getEmail(), createUserDto.getUsername())) {
            return new ResponseEntity<>("User with given username/email already exists", HttpStatus.CONFLICT);
        }

        User user = new User(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));
        user = userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }

    public ResponseEntity<?> login(LoginUserDto loginUserDto){

        if (loginUserDto.getUsername() == null || loginUserDto.getUsername().isEmpty()) {
            return new ResponseEntity<>("Username must not be empty",HttpStatus.BAD_REQUEST);
        }
        if (loginUserDto.getPassword() == null || loginUserDto.getPassword().isEmpty()) {
            return new ResponseEntity<>("Password must not be empty",HttpStatus.BAD_REQUEST);
        }

        Optional<User> optionalUser = userRepository.findByUsername(loginUserDto.getUsername());
        if(optionalUser.isEmpty()){
            return new ResponseEntity<>("User with given username does not exist", HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();
        if(passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())){
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);

            return new ResponseEntity<>("Login successfull !", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid password !", HttpStatus.NOT_FOUND);
        }
    }
}
