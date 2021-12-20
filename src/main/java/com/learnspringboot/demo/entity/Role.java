package com.learnspringboot.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Data
@Table(name = "roles")
@Entity
@NoArgsConstructor
public class Role extends BaseEntity{

    private String name;

    @OneToMany(mappedBy="role")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users;

    public Role(String name){
        this.name = "ROLE_" + name;
    }
}