package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Role;
import com.learnspringboot.demo.respository.PermissionRepository;
import com.learnspringboot.demo.respository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public void save(Permission permission) {
        Optional<Permission> p = permissionRepository.findByModuleAndAction(permission.getModule(), permission.getAction());
        if(p.isPresent() && p.get().getUser().equals(permission.getUser())) return;
        permissionRepository.save(permission);
    }

    public void saveAll(Collection<Permission> permissions){
        for (Permission p: permissions) {
            save(p);
        }
    }

    public void saveAllPerform(List<Permission> permissions){
        permissionRepository.saveAll(permissions);
    }
}
