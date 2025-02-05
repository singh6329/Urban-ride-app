package com.application.urbanRide.strategies.impl;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Payment;
import com.application.urbanRide.entities.enums.PaymentStatus;
import com.application.urbanRide.entities.enums.TransactionMethod;
import com.application.urbanRide.repositories.PaymentRepository;
import com.application.urbanRide.services.WalletService;
import com.application.urbanRide.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Payment payment) {
    Driver driver = payment.getRide().getDriver();
    double commission = payment.getAmount() * PLATFORM_COMMISSION;
    walletService.deductMoneyFromWallet(driver.getUser(), commission,null, payment.getRide(), TransactionMethod.RIDE);
    payment.setPaymentStatus(PaymentStatus.CONFIRMED);
    paymentRepository.save(payment);
    }
}
