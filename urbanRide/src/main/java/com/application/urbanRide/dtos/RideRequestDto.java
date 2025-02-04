package com.application.urbanRide.dtos;

import com.application.urbanRide.entities.enums.PaymentMethod;
import com.application.urbanRide.entities.enums.RideRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RideRequestDto {

    private Long id;

    private PointDto pickUpLocation;

    private PointDto dropOffLocation;

    private PaymentMethod paymentMethod;

    private RiderDto rider;

    private RideRequestStatus status;

    private Double fare;
}
