package com.application.urbanRide.strategies;

import com.application.urbanRide.entities.Payment;

public interface PaymentStrategy {
    Double PLATFORM_COMMISSION = 0.3;
    void processPayment(Payment payment);
}
