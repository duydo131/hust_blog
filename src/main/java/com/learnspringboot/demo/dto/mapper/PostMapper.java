package com.learnspringboot.demo.dto.mapper;

import com.learnspringboot.demo.dto.post.MyPostContentResponseDTO;
import com.learnspringboot.demo.dto.post.PostContentResponseDTO;
import com.learnspringboot.demo.dto.post.PostRequestDTO;
import com.learnspringboot.demo.dto.post.PostUpdateRequestDTO;
import com.learnspringboot.demo.dto.user.UserInfoDTO;
import com.learnspringboot.demo.dto.user.UserPostInfoDTO;
import com.learnspringboot.demo.entity.Post;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.entity.User;
import org.mapstruct.*;

import java.util.UUID;

@Mapper
public interface PostMapper {
    @Mapping(source = "user", target = "user", qualifiedByName = "getUser")
    PostContentResponseDTO mapToPostContentDTO(Post post);

    Post mapToPost(PostRequestDTO post);

    @Mapping(source = "slug", target = "slug", qualifiedByName = "getTitle")
    MyPostContentResponseDTO postToMyPostContentResponseDTO(Post post);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePostFromPostUpdateRequestDTO(PostUpdateRequestDTO dto, @MappingTarget Post post);

    @Named("getUser")
    public static UserPostInfoDTO getUserId(User user) {
        UserPostInfoDTO userPostInfoDTO = new UserPostInfoDTO(
                                                    user.getId(),
                                                    user.getUsername(),
                                                    user.getEmail()
                                                );
        return userPostInfoDTO;
    }

    @Named("getTitle")
    public static String getTitle(Slug slug) {
        return slug.getTitle();
    }
}
