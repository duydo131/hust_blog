package com.learnspringboot.demo.service.infra;

import com.learnspringboot.demo.entity.User;

public interface ICreateUserUseCase {
    void additionalCreateUser(User user, String slug) throws Exception;
}
