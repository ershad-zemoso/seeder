package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.entity.User;
import com.zemoso.seeder.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
class ContractPayoutServiceTest {

    PayoutService payoutService;

    @BeforeEach
    void setUp() {
        payoutService = new ContractPayoutService();
    }

    @Test
    void testCalculatePayoutSummary() {

        User user = new User();
        SummaryDto summary = payoutService.calculatePayoutSummary(List.of(TestDataFactory.getContract1(user), TestDataFactory.getContract2(user)));

        assertEquals(2, summary.getSelectedContractCount().intValue());
        assertEquals("12.00", summary.getFee());
        assertEquals("31680.00", summary.getTotalFeeAmount());
        assertEquals("232320.00", summary.getTotalPayableAmount());
        assertEquals("264000.00", summary.getTotalPayout());
        assertEquals(12, summary.getTerm().intValue());
    }
}
