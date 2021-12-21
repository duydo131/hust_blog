package com.learnspringboot.demo.service.infra.impl;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.service.db.PermissionService;
import com.learnspringboot.demo.service.infra.ICreateUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class NewCreateUserUseCase implements ICreateUserUseCase {
    private PermissionService permissionService;

    public NewCreateUserUseCase(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    public void additionalCreateUser(User user) {
        List<Permission> permissions = new ArrayList<>();

        permissions.add(new Permission("/api/posts", "POST", user));

        permissionService.saveAllPerform(permissions);
    }
}
