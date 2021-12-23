package com.learnspringboot.demo.dto.post;

import com.learnspringboot.demo.dto.slug.SlugResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostResponseDTO {
    private PostContentResponseDTO content;
    private SlugResponseDTO[] slugs;
}
