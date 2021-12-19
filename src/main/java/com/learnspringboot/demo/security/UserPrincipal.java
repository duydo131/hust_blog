package com.learnspringboot.demo.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        user.getRoles().forEach(p -> {
            GrantedAuthority authory = new SimpleGrantedAuthority(p.getName());
            authoriList.add(authory);
        });
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

    public Long getId(){
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
}
