package com.learnspringboot.demo.service.infra.impl;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.service.db.PermissionService;
import com.learnspringboot.demo.service.db.SlugService;
import com.learnspringboot.demo.service.infra.ICreateUserUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

public class NewCreateUserUseCase implements ICreateUserUseCase {
    private final PermissionService permissionService;
    private final SlugService slugService;

    public NewCreateUserUseCase(PermissionService permissionService, SlugService slugService) {
        this.permissionService = permissionService;
        this.slugService = slugService;
    }

    @Override
    @Transactional
    public void additionalCreateUser(User user, String slug) throws Exception {
        List<Slug> children = slugService.getAllChild(slug);

        List<Permission> permissions = new ArrayList<>();

        for (Slug s: children) {
            permissions.add(new Permission("/api/posts/" + s.getTitle(), "POST", user));
        }
        permissionService.saveAllPerform(permissions);
    }
}
