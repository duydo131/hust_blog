package com.learnspringboot.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupInfoDTO extends UserInfoDTO{
    private String rawPassword;
}
