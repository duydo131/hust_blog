package com.learnspringboot.demo.dto.post.statistic;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticPostByUserResponse {
    @JsonAlias("number_of_post")
    private Integer numberOfPost;

    @JsonAlias("number_of_user")
    private Integer numberOfUser;

    @JsonAlias("data")
    private List<PostStatisticByUserResponseObject> data;
}
