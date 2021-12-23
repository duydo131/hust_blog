package com.learnspringboot.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostUpdateRequestDTO {
    private String title;
    private Boolean published;
    private String content;

    @JsonAlias("slug")
    private String slugStr;

    @JsonAlias("short-description")
    private String shortDescription;
}
