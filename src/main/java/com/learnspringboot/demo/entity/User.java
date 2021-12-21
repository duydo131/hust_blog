package com.learnspringboot.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@Table(name = "users")
@Entity
public class User extends BaseEntity{

    @Column(unique = true)
    @NotNull
    private String username;

    private String rawPassword = null;

    @NotNull
    @Column(unique = true)
    private String password;

    @Column(unique = true)
    @NotNull
    private String email;

    @NotNull
    private boolean active = true;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Permission> permission;

    @OneToMany(mappedBy="user", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Post> posts;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
