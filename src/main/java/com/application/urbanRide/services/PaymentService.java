package com.application.urbanRide.services;

import com.application.urbanRide.entities.Payment;
import com.application.urbanRide.entities.Ride;

public interface PaymentService {

    void processPayment(Ride ride);
    Payment createNewPayment(Ride ride);
}
