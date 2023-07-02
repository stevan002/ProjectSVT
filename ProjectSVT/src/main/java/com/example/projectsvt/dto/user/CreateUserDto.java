package com.example.projectsvt.dto.user;

import com.example.projectsvt.model.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserDto {

    private String username;

    private String password;

    private String email;

    private String firstName;

    private String lastName;

    private String image;

}
