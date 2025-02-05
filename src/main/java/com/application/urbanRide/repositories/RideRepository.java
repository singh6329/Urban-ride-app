package com.application.urbanRide.repositories;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.RideRequest;
import com.application.urbanRide.entities.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRepository extends JpaRepository<Ride,Long> {

    Page<Ride> findByDriver(Driver driver, PageRequest pageRequest);

    Page<Ride> findByRider(Rider rider, PageRequest pageRequest);

    List<Ride> findAllByDriver(Driver driver);

    Optional<Ride> findByRideRequest(RideRequest rideRequest);
}
