package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.CashKickDto;
import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.entity.CashKick;
import com.zemoso.seeder.entity.Contract;
import com.zemoso.seeder.entity.User;
import com.zemoso.seeder.exception.NotFoundException;
import com.zemoso.seeder.mapper.CashKickMapper;
import com.zemoso.seeder.model.CashKickRequest;
import com.zemoso.seeder.repository.CashKickRepository;
import com.zemoso.seeder.repository.ContractRepository;
import com.zemoso.seeder.repository.UserRepository;
import com.zemoso.seeder.util.CashKickStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class CashKickService {

    private final PayoutService contractPayoutService;
    private final CashKickRepository cashKickRepository;
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;
    private final CashKickMapper cashKickMapper;

    @Transactional
    public SummaryDto createCashKick(final CashKickRequest cashKickRequest) {

        final User user = userRepository.findById(cashKickRequest.getUserId())
                .orElseThrow(() -> new NotFoundException(String.format("User not found for userId: %s", cashKickRequest.getUserId())));

        final List<Contract> contracts = contractRepository.findAllAvailableById(cashKickRequest.getContractIds());
        if (contracts == null || contracts.isEmpty()) {
            throw new NotFoundException(String.format("No contracts found for contractIds: %s", cashKickRequest.getContractIds()));
        }

        contracts.forEach(contract -> contract.setAvailable(false));
        contractRepository.saveAll(contracts);

        SummaryDto summary = contractPayoutService.calculatePayoutSummary(contracts);

        final CashKick cashKick = new CashKick();
        cashKick.setName(cashKickRequest.getName());
        cashKick.setStatus(CashKickStatus.PENDING.getText());
        cashKick.setTotalFinanced(Double.valueOf(summary.getTotalPayout()));
        cashKick.setTotalReceived(Double.valueOf(summary.getTotalPayableAmount()));
        cashKick.setUser(user);
        cashKick.setMaturity(LocalDate.now().plusMonths(summary.getTerm()));

        cashKickRepository.save(cashKick);

        return summary;
    }

    public List<CashKickDto> getCashKicks(final Long userId) {

        final User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User not found for userId: %s", userId)));
        final Set<CashKick> userCashKicks = user.getCashKicks();
        if (userCashKicks == null || userCashKicks.isEmpty()) {
            throw new NotFoundException(String.format("No cash kicks found for userId: %s", userId));
        }

        return Stream.ofNullable(userCashKicks)
                .flatMap(Collection::stream)
                .map(cashKickMapper::cashKickToCashKickDto)
                .collect(Collectors.toList());
    }

}
