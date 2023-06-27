package com.zemoso.seeder.util;

import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.entity.CashKick;
import com.zemoso.seeder.entity.Contract;
import com.zemoso.seeder.entity.User;
import com.zemoso.seeder.model.CashKickRequest;

import java.time.LocalDate;
import java.util.List;

public class TestDataFactory {

    public static CashKick getCashKick1(final User user) {
        CashKick cashKick = new CashKick();
        cashKick.setName("cash kick-1");
        cashKick.setStatus("Pending");
        cashKick.setUser(user);
        cashKick.setMaturity(LocalDate.now().plusMonths(12));
        cashKick.setTotalReceived(200000D);
        cashKick.setTotalFinanced(180000D);
        return cashKick;
    }

    public static CashKick getCashKick2(final User user) {
        CashKick cashKick = new CashKick();
        cashKick.setName("cash kick-2");
        cashKick.setStatus("Approved");
        cashKick.setUser(user);
        cashKick.setMaturity(LocalDate.now().plusMonths(12));
        cashKick.setTotalReceived(150000D);
        cashKick.setTotalFinanced(135000D);
        return cashKick;
    }

    public static CashKickRequest getCashKickRequest() {
        CashKickRequest cashKickRequest = new CashKickRequest();
        cashKickRequest.setName("cash kick-1");
        cashKickRequest.setUserId(1L);
        cashKickRequest.setContractIds(List.of(1L, 2L));
        return cashKickRequest;
    }

    public static Contract getContract1(final User user) {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setName("Contract-1");
        contract.setAvailable(true);
        contract.setFee(12D);
        contract.setUser(user);
        contract.setType("Monthly");
        contract.setPerPayment(12000D);
        contract.setPayment(126720D);
        contract.setTermLength(12);
        return contract;
    }

    public static Contract getContract2(final User user) {
        Contract contract = new Contract();
        contract.setId(1L);
        contract.setName("Contract-2");
        contract.setAvailable(true);
        contract.setFee(12D);
        contract.setUser(user);
        contract.setType("Monthly");
        contract.setPerPayment(10000D);
        contract.setPayment(105600D);
        contract.setTermLength(12);
        return contract;
    }

    public static SummaryDto getSummary() {
        SummaryDto summaryDto = new SummaryDto();
        summaryDto.setTerm(12);
        summaryDto.setFee("12.00");
        summaryDto.setTotalFeeAmount("15840.00");
        summaryDto.setSelectedContractCount(2);
        summaryDto.setTotalPayableAmount("132000.00");
        summaryDto.setTotalPayout("116160.00");
        return  summaryDto;
    }
}
