package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.CashKickDto;
import com.zemoso.seeder.entity.CashKick;
import com.zemoso.seeder.entity.Contract;
import com.zemoso.seeder.entity.User;
import com.zemoso.seeder.exception.NotFoundException;
import com.zemoso.seeder.mapper.CashKickMapper;
import com.zemoso.seeder.model.CashKickRequest;
import com.zemoso.seeder.repository.CashKickRepository;
import com.zemoso.seeder.repository.ContractRepository;
import com.zemoso.seeder.repository.UserRepository;
import org.junit.Assert;
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
import java.util.Set;

import static com.zemoso.seeder.util.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class CashKickServiceTest {

    @Mock
    PayoutService payoutService;

    @Mock
    CashKickRepository cashKickRepository;

    @Mock
    ContractRepository contractRepository;

    @Mock
    UserRepository userRepository;

    CashKickMapper cashKickMapper = Mappers.getMapper(CashKickMapper.class);

    CashKickService cashKickService;

    @BeforeEach
    void setUp() {
        cashKickService = new CashKickService(payoutService, cashKickRepository, contractRepository, userRepository, cashKickMapper);
    }

    @Test
    void testGetCashKicks() {

        User user = new User();
        CashKick cashKick1 = getCashKick1(user);
        CashKick cashKick2 = getCashKick2(user);

        user.setCashKicks(Set.of(cashKick1, cashKick2));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<CashKickDto> cashKickDtos = cashKickService.getCashKicks(1L);

        Assert.assertEquals(2, cashKickDtos.size());

        if (cashKickDtos.get(0).getStatus().equals("Pending")) {
            Assert.assertEquals("cash kick-1", cashKickDtos.get(0).getName());
            Assert.assertEquals("$200000.00", cashKickDtos.get(0).getTotalReceived());
            Assert.assertEquals("$180000.00", cashKickDtos.get(0).getTotalFinanced());
        } else {
            Assert.assertEquals("cash kick-2", cashKickDtos.get(0).getName());
            Assert.assertEquals("$150000.00", cashKickDtos.get(0).getTotalReceived());
            Assert.assertEquals("$135000.00", cashKickDtos.get(0).getTotalFinanced());
        }

    }

    @Test
    void testGetCashKicksForInvalidUser() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.getCashKicks(1L);
        });

        assertEquals("User not found for userId: 1", ex.getMessage());

    }

    @Test
    void testGetCashKicksWhenNull() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.getCashKicks(1L);
        });

        assertEquals("No cash kicks found for userId: 1", ex.getMessage());

    }

    @Test
    void testGetCashKicksWhenEmpty() {

        User user = new User();
        user.setCashKicks(Collections.emptySet());

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.getCashKicks(1L);
        });

        assertEquals("No cash kicks found for userId: 1", ex.getMessage());

    }

    @Test
    void testSaveCashKick() {

        User user = new User();
        CashKickRequest cashKickRequest = getCashKickRequest();
        List<Contract> selectedContracts = List.of(getContract1(user), getContract2(user));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(contractRepository.findAllAvailableById(List.of(1L, 2L))).thenReturn(selectedContracts);
        Mockito.when(payoutService.calculatePayoutSummary(selectedContracts)).thenReturn(getSummary());

        cashKickService.createCashKick(cashKickRequest);

        Mockito.verify(contractRepository, Mockito.times(1)).saveAll(selectedContracts);
        Mockito.verify(payoutService, Mockito.times(1)).calculatePayoutSummary(selectedContracts);
        Mockito.verify(cashKickRepository, Mockito.times(1)).save(Mockito.any(CashKick.class));
    }

    @Test
    void testSaveCashKickForInvalidContractsWhenNull() {

        CashKickRequest cashKickRequest = getCashKickRequest();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(contractRepository.findAllAvailableById(List.of(1L, 2L))).thenReturn(null);

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.createCashKick(cashKickRequest);
        });

        assertEquals("No contracts found for contractIds: [1, 2]", ex.getMessage());
    }

    @Test
    void testSaveCashKickForInvalidContractsWhenEmpty() {

        CashKickRequest cashKickRequest = getCashKickRequest();
        User user = new User();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(contractRepository.findAllAvailableById(List.of(1L, 2L))).thenReturn(Collections.emptyList());

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.createCashKick(cashKickRequest);
        });

        assertEquals("No contracts found for contractIds: [1, 2]", ex.getMessage());
    }

}
