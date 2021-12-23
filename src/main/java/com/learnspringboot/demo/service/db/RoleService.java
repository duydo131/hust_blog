package com.learnspringboot.demo.service.db;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Role;
import com.learnspringboot.demo.respository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public void saveAll(Collection<Role> roles){ roleRepository.saveAll(roles);}
}
