package com.application.urbanRide.services;

import com.application.urbanRide.entities.RideRequest;

public interface RideRequestService {
    RideRequest findRideRequestById(Long rideRequestId);
    void updateRideRequest(RideRequest rideRequest);
}
