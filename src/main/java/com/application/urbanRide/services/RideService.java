package com.application.urbanRide.services;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.RideRequest;
import com.application.urbanRide.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RideService
{
    Ride getRideById(Long rideId);

    Ride getRideByRideRequest(RideRequest rideRequest);

    Ride createNewRide(RideRequest rideRequest, Driver driver);

    Ride updateRideStatus(Ride ride);

    Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest);

    Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest);

}
