package com.zemoso.seeder.controller;

import com.zemoso.seeder.dto.ContractDto;
import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.service.ContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/contracts")
public class ContractController {

    private final ContractService contractService;

    @GetMapping(path = "/{userId}")
    public List<ContractDto> getUserContracts(@PathVariable final String userId) {
        return contractService.getAllUserContracts(Long.valueOf(userId));
    }

    @GetMapping(path = "/summary")
    public SummaryDto getContractSummary(@RequestParam final List<Long> id) {
        return contractService.getContractsSummary(id);
    }
}
