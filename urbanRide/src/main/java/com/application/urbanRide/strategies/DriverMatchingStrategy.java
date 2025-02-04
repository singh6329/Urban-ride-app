package com.application.urbanRide.strategies;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.RideRequest;

import java.util.List;

public interface DriverMatchingStrategy {

    List<Driver> findMatchingDrivers(RideRequest rideRequest);
}
