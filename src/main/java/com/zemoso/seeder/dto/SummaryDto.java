package com.zemoso.seeder.dto;

import lombok.Data;

@Data
public class SummaryDto {

    private Integer term;
    private Integer selectedContractCount;
    private String totalPayableAmount;
    private String fee;
    private String totalFeeAmount;
    private String totalPayout;

}
