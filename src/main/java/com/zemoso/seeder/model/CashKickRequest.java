package com.zemoso.seeder.model;

import lombok.Data;

import java.util.List;

@Data
public class CashKickRequest {

    private String name;
    private Long userId;
    private List<Long> contractIds;
}
