package com.learnspringboot.demo.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
public class MyPostContentResponseDTO {
    private UUID id;
    private UUID userId;
    private String title;
    private Boolean published;
    private Date publishAt;
    private String shortDescription;
    private String content;
    private Date createAt;
    private Date updateAt;
    private String slug;
    private String image;
}
