package com.learnspringboot.demo.respository;

import com.learnspringboot.demo.entity.Role;
import com.learnspringboot.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}