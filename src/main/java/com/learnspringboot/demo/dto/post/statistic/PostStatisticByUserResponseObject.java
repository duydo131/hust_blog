package com.learnspringboot.demo.dto.post.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostStatisticByUserResponseObject {
    private UUID id;
    private String username;
    private Integer numberOfPost;
}
