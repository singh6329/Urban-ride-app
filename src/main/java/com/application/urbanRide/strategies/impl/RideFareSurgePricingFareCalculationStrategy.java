package com.application.urbanRide.strategies.impl;

import com.application.urbanRide.entities.RideRequest;
import com.application.urbanRide.services.DistanceService;
import com.application.urbanRide.strategies.RideFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RideFareSurgePricingFareCalculationStrategy implements RideFareCalculationStrategy {
    private final DistanceService distanceService;

    @Override
    public double calculateFare(RideRequest rideRequest) {
        Double distance = distanceService.calculateDistance(rideRequest.getPickUpLocation(),rideRequest.getDropOffLocation());
        return distance*SURGE_PRICE_MULTIPLIER;
    }
}
