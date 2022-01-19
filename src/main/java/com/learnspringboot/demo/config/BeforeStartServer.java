package com.learnspringboot.demo.config;

import com.learnspringboot.demo.entity.Role;
import com.learnspringboot.demo.entity.Slug;
import com.learnspringboot.demo.entity.User;
import com.learnspringboot.demo.service.db.RoleService;
import com.learnspringboot.demo.service.db.SlugService;
import com.learnspringboot.demo.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
public class BeforeStartServer implements CommandLineRunner {

    /*
    * hieu suat khong tot
    * nen su dung thu vien migrate database nhu flyway or liquibase
    * */

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private SlugService slugService;

    @Value("${admin.username}")
    private String username;

    @Value("${admin.password}")
    private String password;

    @Value("${admin.email}")
    private String email;

    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    @Transactional
    public void run(String...args) throws Exception {
        if(!ddl.equals("create")) return;
        List<Role> roles = new ArrayList<>();
        RoleEnum[] enumRoles = RoleEnum.values();
        for (RoleEnum enumRole : enumRoles) {
            Role role = new Role();
            role.setName(enumRole.getName());
            roles.add(role);
        }
        roleService.saveAll(roles);

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUsername(username);
        user.setRole(roleService.findByName(RoleEnum.ROLE_ADMIN.getName()).orElseThrow(
                () -> new Exception("Role admin not define"))
        );
        userService.save(user);

        Slug slug = new Slug("home", "Trang chủ", null);
        slugService.save(slug);
    }
}
