package com.learnspringboot.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import com.learnspringboot.demo.entity.Permission;
import com.learnspringboot.demo.entity.Role;
import com.learnspringboot.demo.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.transaction.Transactional;


public class UserPrincipal implements UserDetails{

    private static final long serialVersionUID = 1L;

    private User user;

    public UserPrincipal(User user) {
        this.user = user;
    }

    @Override
    @Transactional
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authoriList = new ArrayList<>();
        GrantedAuthority authory = new SimpleGrantedAuthority(user.getRole().getName());
        authoriList.add(authory);
        return authoriList;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    public UUID getId(){
        return user.getId();
    }

    public String getEmail(){
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    @Transactional
    public Collection<Permission> getPermission(){
        return user.getPermission();
    }

    public Role getRole(){
        return user.getRole();
    }

    public User getUser() {return user;}
}
