package com.zemoso.seeder.dto;

import lombok.Data;

@Data
public class ContractDto {

    private Long id;
    private String name;
    private String type;
    private String perPayment;
    private String payment;
    private int termLength;
    private String fee;
}
