package com.application.urbanRide.services;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Rating;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.Rider;

import java.util.List;

public interface RatingService {
    String rateDriver(Long rideId,Double rating);
    String rateRider(Long rideId, Double rating);
    Rating findRatingByRide(Ride ride);
    List<Rating> findAllByRider(Rider rider);
    List<Rating> findAllByDriver(Driver driver);
    void createNewRating(Rating rating);
}
