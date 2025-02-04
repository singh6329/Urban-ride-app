package com.application.urbanRide.services.impl;

import com.application.urbanRide.custExceptions.BadRequestException;
import com.application.urbanRide.dtos.DriverDto;
import com.application.urbanRide.dtos.DriverRideDto;
import com.application.urbanRide.entities.*;
import com.application.urbanRide.entities.enums.RideRequestStatus;
import com.application.urbanRide.entities.enums.RideStatus;
import com.application.urbanRide.repositories.DriverRepository;
import com.application.urbanRide.repositories.RatingRepository;
import com.application.urbanRide.services.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final RideRequestService rideRequestService;
    private final DriverRepository driverRepository;
    private final RideService rideService;
    private final ModelMapper modelMapper;
    private final PaymentService paymentService;
    private final RatingRepository ratingRepository;

    @Override
    @Transactional
    public DriverRideDto acceptRide(Long rideRequestId) {
        RideRequest rideRequest = rideRequestService.findRideRequestById(rideRequestId);

        if(!rideRequest.getStatus().equals(RideRequestStatus.PENDING))
            throw new RuntimeException("Ride cannot be accepted, Status: "+rideRequest.getStatus());

        rideRequest.setStatus(RideRequestStatus.CONFIRMED);

        Driver driver= getCurrentDriver();
        if(!driver.isAvailable())
            throw new RuntimeException("Driver cannot accept ride due to unavailability!");

        Driver savedDriver = setDriverAvailability(driver,false);
        return modelMapper.map(rideService.createNewRide(rideRequest,savedDriver), DriverRideDto.class);
    }

    @Override
    @Transactional
    public DriverRideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!ride.getDriver().equals(driver))
            throw new RuntimeException("Driver cannot cancel a ride as driver doesn't match!");

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride cannot be cancelled ,invalid status, status: "+ride.getRideStatus());

        ride.setRideStatus(RideStatus.CANCELLED);
        setDriverAvailability(driver,true);
        return modelMapper.map(rideService.updateRideStatus(ride), DriverRideDto.class);

    }

    @Override
    @Transactional
    public DriverRideDto startRide(Long rideId, String otp) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();

        if(!ride.getDriver().equals(driver))
            throw new RuntimeException("Driver cannot start a ride as driver doesn't match!");

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride status is not CONFIRMED, hence cannot be started, status:"+ride.getRideStatus());

        if(!ride.getOtp().equals(otp))
            throw new RuntimeException("OTP doesn't matches!");

        ride.setStartTime(LocalDateTime.now());
        ride.setRideStatus(RideStatus.ONGOING);
        paymentService.createNewPayment(ride);
        Rating rating = Rating.builder().driverRating(null).riderRating(null).driver(ride.getDriver())
                .rider(ride.getRider()).ride(ride).build();
        ratingRepository.save(rating);
        return modelMapper.map(rideService.updateRideStatus(ride), DriverRideDto.class);
    }

    @Override
    @Transactional
    public DriverRideDto endRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Driver driver = getCurrentDriver();
        if(!ride.getDriver().equals(driver))
            throw new RuntimeException("Driver cannot cancel this ride as Driver doesn't matches");

        if(!ride.getRideStatus().equals(RideStatus.ONGOING))
            throw new RuntimeException("Ride cannot be ended, status: "+ride.getRideStatus());

        setDriverAvailability(driver,true);
        ride.setEndTime(LocalDateTime.now());
        ride.setRideStatus(RideStatus.ENDED);
        paymentService.processPayment(ride);
        return modelMapper.map(rideService.updateRideStatus(ride), DriverRideDto.class);
    }


    @Override
    public DriverDto getMyProfile() {
        Driver driver = getCurrentDriver();
        return modelMapper.map(driver,DriverDto.class);
    }

    @Override
    public Page<DriverRideDto> getAllMyRides(PageRequest pageRequest) {
        return rideService.getAllRidesOfDriver(getCurrentDriver(), pageRequest).map(ride ->
                modelMapper.map(ride,DriverRideDto.class));
    }

    @Override
    public Driver getCurrentDriver() {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authenticatedUser.getPrincipal();
         return driverRepository.findByUser(user).orElseThrow(()->new RuntimeException("Unable to get Profile!"));
    }

    @Override
    public Driver setDriverAvailability(Driver driver, Boolean availability) {
        driver.setAvailable(availability);
        return driverRepository.save(driver);
    }

    @Override
    public void updateDriverRating(Driver driver,Double cumulativeRating) {
        driver.setRating(cumulativeRating);
        driverRepository.save(driver);
    }

    @Override
    public Driver createNewDriver(Driver driver) {
        return driverRepository.save(driver);
    }

    @Override
    public DriverDto updateDriverAvailability(Boolean availability) {
        Driver driver = getCurrentDriver();
        if(driver.isAvailable() == availability)
            throw new BadRequestException("Driver availability is already set to: "+driver.isAvailable());

        List<DriverRideDto> rideDtos = getAllMyRides(PageRequest.of(0,10, Sort.by(Sort.Direction.DESC,"createdTime"))).getContent();

        List<DriverRideDto> updatedRideDtos = rideDtos.stream().filter(ride->ride.getRideStatus().equals(RideStatus.ONGOING) || ride.getRideStatus().equals(RideStatus.CONFIRMED)).toList();

        if (!updatedRideDtos.isEmpty() && availability)
            throw new RuntimeException("Availability cannot be set to true as Driver's ride is already in ongoing or confirmed status! ");

        return modelMapper.map(setDriverAvailability(driver,availability),DriverDto.class);
    }
}
