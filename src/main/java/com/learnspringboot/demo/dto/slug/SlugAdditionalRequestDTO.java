package com.learnspringboot.demo.dto.slug;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SlugAdditionalRequestDTO {

    @NotNull
    @JsonAlias("parent-id")
    private UUID parentID;

    @NotNull
    private String title;

    @NotNull
    private String name;
}
