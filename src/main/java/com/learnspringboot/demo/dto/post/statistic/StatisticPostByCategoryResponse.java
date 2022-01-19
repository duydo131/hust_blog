package com.learnspringboot.demo.dto.post.statistic;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.learnspringboot.demo.dto.post.statistic.PostStatisticByCategoryResponseObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticPostByCategoryResponse {
    @JsonAlias("number_of_post")
    private Integer numberOfPost;

    @JsonAlias("number_of_post_published")
    private Integer numberOfPostPublished;

    @JsonAlias("number_of_post_not_published")
    private Integer numberOfPostNotPublished;

    @JsonAlias("data")
    private List<PostStatisticByCategoryResponseObject> data;
}
