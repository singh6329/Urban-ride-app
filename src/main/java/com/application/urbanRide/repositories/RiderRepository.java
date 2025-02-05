package com.application.urbanRide.repositories;

import com.application.urbanRide.entities.Rider;
import com.application.urbanRide.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RiderRepository extends JpaRepository<Rider,Long> {

    Optional<Rider> findByUser(User user);
}
