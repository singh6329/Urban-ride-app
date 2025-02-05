package com.application.urbanRide.services.impl;

import com.application.urbanRide.custExceptions.BadRequestException;
import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Rating;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.Rider;
import com.application.urbanRide.entities.enums.RideStatus;
import com.application.urbanRide.repositories.RatingRepository;
import com.application.urbanRide.services.DriverService;
import com.application.urbanRide.services.RatingService;
import com.application.urbanRide.services.RideService;
import com.application.urbanRide.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RiderService riderService;
    private final RatingRepository ratingRepository;
    private final RideService rideService;
    private final DriverService driverService;

    @Override
    @Transactional
    public String rateDriver(Long rideId,Double ratingValue)  //this would be called by Rider controller
    {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = riderService.getCurrentRider();
        Rating rating = findRatingByRide(ride);

        if(rating.getDriverRating() != null)
            throw new RuntimeException("Rating has already been provided for Driver for this Ride!");

        if(!ride.getRider().equals(rider))
            throw new RuntimeException("Cannot give rating as Rider does not matches with Ride rider.");

        if(!ride.getRideStatus().equals(RideStatus.ENDED))
            throw new RuntimeException("You can only give rating if ride is ended. Current status:"+ride.getRideStatus());

        rating.setDriverRating(ratingValue);
        ratingRepository.save(rating);
        Driver driver = ride.getDriver();
        Double cumulativeRating = findAllByDriver(driver).stream().mapToDouble(Rating::getDriverRating).average().orElse(ratingValue);
        driverService.updateDriverRating(driver, cumulativeRating);
        return "Thanks for rating! Your rating has been recorded!";
    }

    @Override
    public String rateRider(Long rideId, Double ratingValue) // this method would be called by Driver controller
    {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = driverService.getCurrentDriver();
        Rating rating = findRatingByRide(ride);

        if(rating.getRiderRating()!=null)
            throw new BadRequestException("Rating has already been provided for Rider!");

        if(!ride.getDriver().equals(driver))
            throw new RuntimeException("Cannot give rating as Driver does not matches with Ride driver.");

        if(!ride.getRideStatus().equals(RideStatus.ENDED))
            throw new BadRequestException("You can only give rating if ride is ended. Current status:"+ride.getRideStatus());

        rating.setRiderRating(ratingValue);
        ratingRepository.save(rating);
        Double cumulativeRating = ratingRepository.findAllByRider(ride.getRider()).stream().mapToDouble(Rating::getRiderRating).average().orElse(ratingValue);
        riderService.updateRiderRating(ride.getRider(),cumulativeRating);
        return "Thanks for rating! Your rating has been recorded!";

    }

    @Override
    public Rating findRatingByRide(Ride ride) {
        return ratingRepository.findByRide(ride).orElse(null);
    }

    @Override
    public List<Rating> findAllByRider(Rider rider) {
        return ratingRepository.findAllByRider(rider);
    }

    @Override
    public List<Rating> findAllByDriver(Driver driver) {
        return ratingRepository.findAllByDriver(driver);
    }

    @Override
    public void createNewRating(Rating rating) {
        ratingRepository.save(rating);
    }


}
