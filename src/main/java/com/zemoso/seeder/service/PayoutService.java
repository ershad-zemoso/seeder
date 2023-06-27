package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.entity.Contract;

import java.util.List;

public interface PayoutService {

    SummaryDto calculatePayoutSummary(final List<Contract> contracts);
}
