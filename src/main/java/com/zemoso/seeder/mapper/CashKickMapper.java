package com.zemoso.seeder.mapper;

import com.zemoso.seeder.dto.CashKickDto;
import com.zemoso.seeder.entity.CashKick;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CashKickMapper {

    @Mapping(source = "totalReceived", target = "totalReceived", numberFormat = "$#.00")
    @Mapping(source = "totalFinanced", target = "totalFinanced", numberFormat = "$#.00")
    @Mapping(source = "maturity", target = "maturityDate", dateFormat = "MMM dd, yyyy")
    CashKickDto cashKickToCashKickDto(CashKick cashKick);
}
