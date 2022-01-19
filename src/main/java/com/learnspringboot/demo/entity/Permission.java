package com.learnspringboot.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Table(name = "permissions")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseEntity{

    @NotNull
    private String module;

    @NotNull
    private String action;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String slug;

    private UUID slugId;
}
