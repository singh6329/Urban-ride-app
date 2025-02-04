package com.application.urbanRide.services;

import com.application.urbanRide.dtos.DriverDto;
import com.application.urbanRide.dtos.DriverRideDto;
import com.application.urbanRide.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface DriverService {
    DriverRideDto acceptRide(Long rideRequestId);

    DriverRideDto cancelRide(Long rideId);

    DriverRideDto startRide(Long rideId, String otp);

    DriverRideDto endRide(Long rideId);

    DriverDto getMyProfile();

    Page<DriverRideDto> getAllMyRides(PageRequest pageRequest);

    Driver getCurrentDriver();

    Driver setDriverAvailability(Driver driver, Boolean availability); //internal method to update driver availability

    void updateDriverRating(Driver driver,Double cumulativeRating);

    Driver createNewDriver(Driver driver);

    DriverDto updateDriverAvailability(Boolean availability); // method called by driver controller to update driver availability
}
