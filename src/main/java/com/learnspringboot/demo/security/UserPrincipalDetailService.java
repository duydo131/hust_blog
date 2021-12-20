package com.learnspringboot.demo.security;

import com.learnspringboot.demo.entity.User;

import com.learnspringboot.demo.service.db.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserPrincipalDetailService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Override
    public UserPrincipal loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username).orElse(null);
        return new UserPrincipal(user);
    }

}
