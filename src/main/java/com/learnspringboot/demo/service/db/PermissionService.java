package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Role;
import com.learnspringboot.demo.respository.PermissionRepository;
import com.learnspringboot.demo.respository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public void save(Permission permission) {
        permissionRepository.save(permission);
    }

    public void saveAll(Collection<Permission> permissions){
        permissionRepository.saveAll(permissions);
    }
}
