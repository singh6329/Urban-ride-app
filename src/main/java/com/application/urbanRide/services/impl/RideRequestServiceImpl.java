package com.application.urbanRide.services.impl;

import com.application.urbanRide.entities.RideRequest;
import com.application.urbanRide.repositories.RideRequestRepository;
import com.application.urbanRide.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class RideRequestServiceImpl implements RideRequestService {

    private final RideRequestRepository rideRequestRepository;

    @Override
    public RideRequest findRideRequestById(Long rideRequestId) {
        return rideRequestRepository.findById(rideRequestId).orElseThrow(()->
                new NoSuchElementException("RideRequest not found with Id:"+rideRequestId));
    }

    @Override
    public void updateRideRequest(RideRequest rideRequest) {
        RideRequest toBeUpdated = rideRequestRepository.findById(rideRequest.getId()).orElseThrow(()->
                new RuntimeException("RideRequest Id is not found!"));
        rideRequestRepository.save(toBeUpdated);
    }
}
