package com.learnspringboot.demo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Table(name = "slugs")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Slug extends BaseEntity{
    @OneToOne(mappedBy = "slug")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Slug parent;

    @NotNull
    @Column(unique = true)
    private String title;

    @NotNull
    private String name;

    public Slug(String title, String name, Slug slugParent) {
        super();
        this.title = title;
        this.name = name;
        parent = slugParent;
    }
}