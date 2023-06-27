package com.zemoso.seeder.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    @Column(name = "per_payment")
    private Double perPayment;
    private Double payment;
    @Column(name = "term_length")
    private int termLength;
    private Double fee;
    private boolean available;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
