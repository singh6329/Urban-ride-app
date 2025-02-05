package com.application.urbanRide.dtos;

import com.application.urbanRide.entities.enums.PaymentMethod;
import com.application.urbanRide.entities.enums.RideStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RiderRideDto {
    private Long id;

    private RiderDto rider;

    private DriverDto driver;

    private RideStatus rideStatus;

    private PointDto pickUpLocation;

    private PointDto dropOffLocation;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double fare;

    private String otp;

    private PaymentMethod paymentMethod;
}
