package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.ContractDto;
import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.entity.Contract;
import com.zemoso.seeder.entity.User;
import com.zemoso.seeder.exception.NotFoundException;
import com.zemoso.seeder.mapper.ContractMapper;
import com.zemoso.seeder.repository.ContractRepository;
import com.zemoso.seeder.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.zemoso.seeder.util.TestDataFactory.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ContractServiceTest {

    @Mock
    PayoutService payoutService;

    @Mock
    ContractRepository contractRepository;

    @Mock
    UserRepository userRepository;

    ContractMapper contractMapper = Mappers.getMapper(ContractMapper.class);

    ContractService contractService;

    @BeforeEach
    void setUp() {
        contractService = new ContractService(payoutService, contractRepository, userRepository, contractMapper);
    }

    @Test
    void testGetAllUserContracts() {

        User user = new User();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(contractRepository.findAllAvailableByUserId(user)).thenReturn(List.of(getContract1(user), getContract2(user)));


        List<ContractDto> contractDtos = contractService.getAllUserContracts(1L);

        assertEquals(2, contractDtos.size());
    }

    @Test
    void testGetAllUserContractsWhenNull() {

        User user = new User();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(contractRepository.findAllAvailableByUserId(user)).thenReturn(null);

        Exception ex = assertThrows(NotFoundException.class, () -> contractService.getAllUserContracts(1L));

        Assertions.assertEquals("No contracts found for User Id: 1", ex.getMessage());
    }

    @Test
    void testGetAllUserContractsWhenEmpty() {

        User user = new User();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(contractRepository.findAllAvailableByUserId(user)).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(NotFoundException.class, () -> contractService.getAllUserContracts(1L));

        Assertions.assertEquals("No contracts found for User Id: 1", ex.getMessage());
    }

    @Test
    void testGetContractsSummary() {
        User user = new User();
        List<Contract> contracts = List.of(getContract1(user), getContract2(user));
        Mockito.when(contractRepository.findAllAvailableById(List.of(1L, 2L))).thenReturn(contracts);
        Mockito.when(payoutService.calculatePayoutSummary(contracts)).thenReturn(getSummary());

        SummaryDto summary = contractService.getContractsSummary(List.of(1L, 2L));

        assertEquals(2, summary.getSelectedContractCount().intValue());
        assertEquals("12.00", summary.getFee());
        assertEquals("15840.00", summary.getTotalFeeAmount());
        assertEquals("132000.00", summary.getTotalPayableAmount());
        assertEquals("116160.00", summary.getTotalPayout());
        assertEquals(12, summary.getTerm().intValue());
    }

    @Test
    void testGetContractsSummaryWhenNull() {
        User user = new User();
        List<Contract> contracts = List.of(getContract1(user), getContract2(user));
        Mockito.when(contractRepository.findAllAvailableById(List.of(1L, 2L))).thenReturn(null);

        Exception ex = assertThrows(NotFoundException.class, () -> contractService.getContractsSummary(List.of(1L, 2L)));
        Assertions.assertEquals("No contracts found for contractIds: [1, 2]", ex.getMessage());
    }

    @Test
    void testGetContractsSummaryWhenEmpty() {
        User user = new User();
        List<Contract> contracts = List.of(getContract1(user), getContract2(user));
        Mockito.when(contractRepository.findAllAvailableById(List.of(1L, 2L))).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(NotFoundException.class, () -> contractService.getContractsSummary(List.of(1L, 2L)));
        Assertions.assertEquals("No contracts found for contractIds: [1, 2]", ex.getMessage());
    }
}
