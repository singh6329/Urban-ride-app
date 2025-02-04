package com.application.urbanRide.services;

import com.application.urbanRide.dtos.RideRequestDto;
import com.application.urbanRide.dtos.RiderDto;
import com.application.urbanRide.dtos.RiderRideDto;
import com.application.urbanRide.entities.Rider;
import com.application.urbanRide.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface RiderService {
    RideRequestDto requestRide(RideRequestDto rideRequestDto);

    String getRideRequestStatus(Long rideId);

    RideRequestDto cancelRideRequest(Long rideRequestId);

    RiderRideDto cancelRide(Long rideId);

    RiderDto getMyProfile();

    Page<RiderRideDto> getAllMyRides(PageRequest pageRequest);

    Rider createRider(User user);

    Rider getCurrentRider();

    void updateRiderRating(Rider rider,Double ratingValue);
}
