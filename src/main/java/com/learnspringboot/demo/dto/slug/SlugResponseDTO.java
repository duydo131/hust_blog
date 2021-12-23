package com.learnspringboot.demo.dto.slug;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SlugResponseDTO {
    private UUID parentId;
    private String title;
    private String name;
}
