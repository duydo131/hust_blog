package com.learnspringboot.demo.dto.post.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStatisticByCategoryResponseObject {
    private UUID id;
    private String title;
    private String name;
    private Integer numberOfPost;
    private Integer numberOfPublished;
    private Integer numberOfNotPublished;
}
