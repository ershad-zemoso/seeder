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

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.zemoso.seeder.util.TestDataFactory.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CashKickServiceTest {

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
    public void setUp() {
        cashKickService = new CashKickService(payoutService, cashKickRepository, contractRepository, userRepository, cashKickMapper);
    }

    @Test
    public void testGetCashKicks() {

        User user = new User();
        CashKick cashKick1 = getCashKick1(user);
        CashKick cashKick2 = getCashKick2(user);

        user.setCashKicks(Set.of(cashKick1, cashKick2));

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        List<CashKickDto> cashKickDtos = cashKickService.getCashKicks(1L);

        Assert.assertEquals(cashKickDtos.size(), 2);

        if (cashKickDtos.get(0).getStatus().equals("Pending")) {
            Assert.assertEquals(cashKickDtos.get(0).getName(), "cash kick-1");
            Assert.assertEquals(cashKickDtos.get(0).getTotalReceived(), "$200000.00");
            Assert.assertEquals(cashKickDtos.get(0).getTotalFinanced(), "$180000.00");
        } else {
            Assert.assertEquals(cashKickDtos.get(0).getName(), "cash kick-2");
            Assert.assertEquals(cashKickDtos.get(0).getTotalReceived(), "$150000.00");
            Assert.assertEquals(cashKickDtos.get(0).getTotalFinanced(), "$135000.00");
        }

    }

    @Test
    public void testGetCashKicksForInvalidUser() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.getCashKicks(1L);
        });

        assertTrue(ex.getMessage().equals("User not found for userId: 1"));

    }

    @Test
    public void testGetCashKicksWhenEmpty() {

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.getCashKicks(1L);
        });

        assertTrue(ex.getMessage().equals("No cash kicks found for userId: 1"));

    }

    @Test
    public void testSaveCashKick() {

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
    public void testSaveCashKickForInvalidContracts() {

        CashKickRequest cashKickRequest = getCashKickRequest();

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));
        Mockito.when(contractRepository.findAllAvailableById(List.of(1L, 2L))).thenReturn(null);

        Exception ex = assertThrows(NotFoundException.class, () -> {
            cashKickService.createCashKick(cashKickRequest);
        });

        assertTrue(ex.getMessage().equals("No contracts found for contractIds: [1, 2]"));
    }

}
