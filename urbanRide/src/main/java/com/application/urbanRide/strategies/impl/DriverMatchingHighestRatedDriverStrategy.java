package com.application.urbanRide.strategies.impl;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.RideRequest;
import com.application.urbanRide.repositories.DriverRepository;
import com.application.urbanRide.strategies.DriverMatchingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverMatchingHighestRatedDriverStrategy implements DriverMatchingStrategy {
    private final DriverRepository driverRepository;
    @Override
    public List<Driver> findMatchingDrivers(RideRequest rideRequest) {
        return driverRepository.findTenHighestRatedDrivers(rideRequest.getPickUpLocation());
    }
}
