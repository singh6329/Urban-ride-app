package com.application.urbanRide.strategies;

import com.application.urbanRide.entities.RideRequest;

public interface RideFareCalculationStrategy {
    int MULTIPLIER = 10;
    int SURGE_PRICE_MULTIPLIER = 20;
    double calculateFare(RideRequest rideRequest);
}
