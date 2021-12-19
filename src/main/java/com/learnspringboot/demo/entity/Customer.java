package com.learnspringboot.demo.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Table(name = "customers")
@Entity
public class Customer {

    @Id
    private String id;

    private String name;

    private Integer age;
}
