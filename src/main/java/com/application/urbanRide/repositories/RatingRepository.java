package com.application.urbanRide.repositories;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Rating;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.Rider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating,Long> {

    Optional<Rating> findByRide(Ride ride);
    List<Rating> findAllByRider(Rider rider);
    List<Rating> findAllByDriver(Driver driver);
}
