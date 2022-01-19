package com.learnspringboot.demo.dto.mapper;

import com.learnspringboot.demo.dto.slug.SlugResponseDTO;
import com.learnspringboot.demo.dto.slug.SlugResponseInfomationDTO;
import com.learnspringboot.demo.entity.Slug;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper
public interface SlugMapper {
    @Mapping(source = "parent", target = "parentId", qualifiedByName = "getSlugId")
    SlugResponseDTO mapToSlugResponseDTO(Slug slug);

    SlugResponseInfomationDTO toSlugResponseInfomationDto(Slug slug);

    @Named("getSlugId")
    public static UUID getSlugId(Slug slug) {
        if(slug != null) return slug.getId();
        return null;
    }
}
