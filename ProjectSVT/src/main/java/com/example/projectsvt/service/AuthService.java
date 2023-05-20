package com.example.projectsvt.service;

import com.example.projectsvt.dto.user.CreateUserDto;
import com.example.projectsvt.model.User;
import com.example.projectsvt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
}
