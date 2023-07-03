package com.example.projectsvt.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostDto {

    private String content;

    private Long createdBy;

    private Long containedBy;

//    private String image;

}
