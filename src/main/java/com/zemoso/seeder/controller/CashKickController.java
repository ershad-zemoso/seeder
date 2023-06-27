package com.zemoso.seeder.controller;

import com.zemoso.seeder.dto.CashKickDto;
import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.model.CashKickRequest;
import com.zemoso.seeder.service.CashKickService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/cash-kicks")
public class CashKickController {

    private final CashKickService cashKickService;

    @GetMapping("/{userId}")
    public List<CashKickDto> getUserCashKicks(@PathVariable final Long userId) {
        return cashKickService.getCashKicks(userId);
    }

    @PostMapping
    public SummaryDto createCashKick(@RequestBody CashKickRequest cashKickRequest) {
        return cashKickService.createCashKick(cashKickRequest);
    }
}
