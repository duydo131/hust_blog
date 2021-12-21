package com.learnspringboot.demo.service.infra.impl;

import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.service.infra.ICreateUserUseCase;

public class NoActionCreateUseCase implements ICreateUserUseCase {
    @Override
    public void additionalCreateUser(User user) {

    }
}
