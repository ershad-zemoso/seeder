package com.zemoso.seeder.dto;

import lombok.Data;

@Data
public class CashKickDto {

    private String name;
    private String status;
    private String maturityDate;
    private String totalReceived;
    private String totalFinanced;
}
