package com.application.urbanRide.strategies.impl;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Payment;
import com.application.urbanRide.entities.Rider;
import com.application.urbanRide.entities.enums.PaymentStatus;
import com.application.urbanRide.entities.enums.TransactionMethod;
import com.application.urbanRide.repositories.PaymentRepository;
import com.application.urbanRide.services.WalletService;
import com.application.urbanRide.strategies.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WalletPaymentStrategy implements PaymentStrategy {

    private final WalletService walletService;
    private final PaymentRepository paymentRepository;

    @Override
    @Transactional
    public void processPayment(Payment payment) {
        Driver driver = payment.getRide().getDriver();
        Rider rider = payment.getRide().getRider();
        Double commission = payment.getAmount()*PLATFORM_COMMISSION;
        Double moneyToBeAddedToDriver = payment.getAmount()-commission;
        walletService.deductMoneyFromWallet(rider.getUser(), payment.getAmount(), null, payment.getRide(), TransactionMethod.RIDE);
        walletService.addFundsToWallet(driver.getUser(), moneyToBeAddedToDriver, null, payment.getRide(), TransactionMethod.RIDE);
        payment.setPaymentStatus(PaymentStatus.CONFIRMED);
        paymentRepository.save(payment);
    }
}
