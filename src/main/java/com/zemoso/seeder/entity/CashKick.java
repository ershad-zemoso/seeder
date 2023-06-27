package com.zemoso.seeder.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "cash_kick")
public class CashKick {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String status;
    private LocalDate maturity;
    private Double totalReceived;
    private Double totalFinanced;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
