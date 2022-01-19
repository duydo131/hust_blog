package com.learnspringboot.demo.dto.slug;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlugResponseInfomationDTO {
    private UUID id;
    private String name;
    private String title;
}
