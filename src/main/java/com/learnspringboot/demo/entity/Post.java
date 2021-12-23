package com.learnspringboot.demo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Table(name = "posts")
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude="slug")
@ToString(exclude = "slug")
public class Post extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    private String title;

    private Boolean published = false;

    @Temporal(TemporalType.TIMESTAMP)
    private Date publishAt;

    @NotNull
    private String shortDescription;

    @NotNull
    private String content;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "slug_id", referencedColumnName = "id")
    private Slug slug;
}
