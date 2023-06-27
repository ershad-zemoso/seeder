package com.zemoso.seeder.service;

import com.zemoso.seeder.dto.SummaryDto;
import com.zemoso.seeder.entity.Contract;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.zemoso.seeder.util.Constants.cashFormat;

@Service
public class ContractPayoutService implements PayoutService {

    @Override
    public SummaryDto calculatePayoutSummary(List<Contract> contracts) {
        double totalPaymentWithoutFees = 0;
        double totalFeeAmount = 0;
        double totalPaymentWithFees = 0;
        double totalFeePercent = 0;

        for (Contract contract : contracts) {
            double paymentWithoutFees = contract.getPerPayment() * contract.getTermLength();
            double feeAmount = paymentWithoutFees - contract.getPayment();

            totalPaymentWithoutFees += paymentWithoutFees;
            totalFeeAmount += feeAmount;
            totalPaymentWithFees += contract.getPayment();
            totalFeePercent += contract.getFee();
        }

        final int termLength = contracts.get(0).getTermLength();

        SummaryDto summaryDto = new SummaryDto();
        summaryDto.setSelectedContractCount(contracts.size());
        summaryDto.setTerm(termLength);
        summaryDto.setTotalPayout(cashFormat.format(totalPaymentWithoutFees));
        summaryDto.setTotalPayableAmount(cashFormat.format(totalPaymentWithFees));
        summaryDto.setTotalFeeAmount(cashFormat.format(totalFeeAmount));
        summaryDto.setFee(cashFormat.format(totalFeePercent / summaryDto.getSelectedContractCount()));

        return summaryDto;
    }
}
