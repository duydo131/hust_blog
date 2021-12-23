package com.learnspringboot.demo.dto.post;

import com.learnspringboot.demo.dto.slug.SlugResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class ListPostResponseDTO {
    private Page<PostContentResponseDTO> content;
    private SlugResponseDTO[] slugs;
}
