package com.learnspringboot.demo.dto.post;

import com.learnspringboot.demo.dto.user.UserPostInfoDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PostContentResponseDTO {
    private UUID id;
    private UserPostInfoDTO user;
    private String title;
    private Boolean published;
    private Date publishAt;
    private String shortDescription;
    private String content;
    private Date createAt;
    private Date updateAt;
    private String image;
}
