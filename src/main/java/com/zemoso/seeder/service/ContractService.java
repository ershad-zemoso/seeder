package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.ContractDto;
import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.entity.Contract;
import com.zemoso.seeder.entity.User;
import com.zemoso.seeder.exception.NotFoundException;
import com.zemoso.seeder.mapper.ContractMapper;
import com.zemoso.seeder.repository.ContractRepository;
import com.zemoso.seeder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ContractService {

    private final PayoutService contractPayoutService;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final ContractMapper contractMapper;

    public List<ContractDto> getAllUserContracts(final Long userId) {
        final Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            final List<Contract> contracts = contractRepository.findAllAvailableByUserId(user.get());
            if (contracts != null && !contracts.isEmpty()) {
                return contracts.stream()
                        .filter(c -> c.isAvailable())
                        .map(contractMapper::contractToContractDto)
                        .collect(Collectors.toList());
            }
        }
        throw new NotFoundException(String.format("No contracts found for User Id: %s", userId));
    }

    public SummaryDto getContractsSummary(final List<Long> contractIds) {
        final List<Contract> contracts = contractRepository.findAllAvailableById(contractIds);

        if (contracts == null || contracts.isEmpty()) {
            throw new NotFoundException(String.format("No contracts found for contractIds: %s", contractIds));
        }

        return contractPayoutService.calculatePayoutSummary(contracts);
    }

}
