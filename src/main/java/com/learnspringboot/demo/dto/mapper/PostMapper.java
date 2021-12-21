package com.learnspringboot.demo.dto.mapper;

import com.learnspringboot.demo.dto.post.PostDTO;
import com.learnspringboot.demo.entity.Post;
import org.mapstruct.Mapper;

@Mapper
public interface PostMapper {
    PostDTO mapToPostDTO(Post post);
}
