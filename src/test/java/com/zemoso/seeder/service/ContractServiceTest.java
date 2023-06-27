package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.ContractDto;
import com.zemoso.seeder.entity.User;
import com.zemoso.seeder.exception.NotFoundException;
import com.zemoso.seeder.mapper.ContractMapper;
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

import static com.zemoso.seeder.util.TestDataFactory.getContract1;
import static com.zemoso.seeder.util.TestDataFactory.getContract2;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ContractServiceTest {

    @Mock
    PayoutService payoutService;

    @Mock
    ContractRepository contractRepository;

    @Mock
    UserRepository userRepository;

    ContractMapper contractMapper = Mappers.getMapper(ContractMapper.class);

    ContractService contractService;

    @BeforeEach
    public void setUp() {
        contractService = new ContractService(payoutService, contractRepository, userRepository, contractMapper);
    }

    @Test
    public void testGetAllUserContracts() {

        User user = new User();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(contractRepository.findAllAvailableByUserId(user)).thenReturn(List.of(getContract1(user), getContract2(user)));


        List<ContractDto> contractDtos = contractService.getAllUserContracts(1L);

        Assert.assertEquals(contractDtos.size(), 2);
    }

    @Test
    public void testGetAllUserContractsForNoContrats() {

        User user = new User();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(contractRepository.findAllAvailableByUserId(user)).thenReturn(null);

        Exception ex = assertThrows(NotFoundException.class, () -> {
            contractService.getAllUserContracts(1L);
        });

        assertTrue(ex.getMessage().equals("No contracts found for User Id: 1"));
    }
}
