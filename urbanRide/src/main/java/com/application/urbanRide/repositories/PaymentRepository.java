package com.application.urbanRide.repositories;

import com.application.urbanRide.entities.Payment;
import com.application.urbanRide.entities.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {
    Optional<Payment> findByRide(Ride ride);

}
