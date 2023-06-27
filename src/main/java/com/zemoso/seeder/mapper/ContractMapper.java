package com.zemoso.seeder.mapper;

import com.zemoso.seeder.dto.ContractDto;
import com.zemoso.seeder.entity.Contract;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractMapper {

    @Mapping(source = "perPayment", target = "perPayment", numberFormat = "$#.00")
    @Mapping(source = "payment", target = "payment", numberFormat = "$#.00")
    @Mapping(source = "fee", target = "fee", numberFormat = "#.00")
    ContractDto contractToContractDto(final Contract contract);

}
