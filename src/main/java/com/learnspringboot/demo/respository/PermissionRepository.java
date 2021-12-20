package com.learnspringboot.demo.respository;

import com.learnspringboot.demo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
