package com.learnspringboot.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoDTO {
    private UUID id;
    private String username;
    private String email;
    private String rawPassword;
    private Integer numPosts = 1;
}
