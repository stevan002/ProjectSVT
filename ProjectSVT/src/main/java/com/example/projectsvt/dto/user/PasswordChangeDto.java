package com.example.projectsvt.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDto {
    private String username;

    private String oldPassword;

    private String newPassword;
}
