package com.learnspringboot.demo.dto.post;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {
    @NotNull
    private String title;

    @NotNull
    @JsonAlias("short-description")
    private String shortDescription;

    @NotNull
    private String content;

    private Boolean published = false;

    private String image;
}
