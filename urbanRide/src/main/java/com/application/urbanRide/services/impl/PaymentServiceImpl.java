package com.application.urbanRide.services.impl;

import com.application.urbanRide.custExceptions.ResourceNotFoundException;
import com.application.urbanRide.entities.Payment;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.enums.PaymentStatus;
import com.application.urbanRide.repositories.PaymentRepository;
import com.application.urbanRide.services.PaymentService;
import com.application.urbanRide.strategies.PaymentStrategyManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentStrategyManager paymentStrategyManager;
    private final PaymentRepository paymentRepository;

    @Override
    public void processPayment(Ride ride) {
        Payment payment = paymentRepository.findByRide(ride).orElseThrow(()->
                new ResourceNotFoundException("Unable to find the Payment associated with the Ride!"));
    paymentStrategyManager.getPaymentStrategy(ride.getPaymentMethod()).processPayment(payment);
    }

    @Override
    public Payment createNewPayment(Ride ride) {
        Payment payment = Payment.builder()
                                        .paymentStatus(PaymentStatus.PENDING)
                                        .paymentMethod(ride.getPaymentMethod())
                                        .amount(ride.getFare())
                                        .ride(ride)
                                        .build();
        return paymentRepository.save(payment);
    }

}
