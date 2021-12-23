package com.learnspringboot.demo.dto.mapper;

import com.learnspringboot.demo.dto.auth.SignupDTO;
import com.learnspringboot.demo.dto.user.UserInfoDTO;
import com.learnspringboot.demo.dto.user.UserSignupInfoDTO;
import com.learnspringboot.demo.entity.User;
import org.mapstruct.Mapper;

@Mapper
public interface UserMapper {
    UserInfoDTO userToUserInfoDTO(User user);
    User SignupDTOToUser(SignupDTO signupDTO);
    UserSignupInfoDTO userToUserSignupInfo(User user);
}
