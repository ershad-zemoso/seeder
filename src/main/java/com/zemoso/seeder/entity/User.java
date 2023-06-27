package com.zemoso.seeder.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate dob;
    private String company;
    private String designation;

    @OneToMany(mappedBy = "user")
    private Set<CashKick> cashKicks;

    @OneToMany(mappedBy = "user")
    private Set<Contract> contracts;
}
