package com.application.urbanRide.services.impl;

import com.application.urbanRide.custExceptions.BadRequestException;
import com.application.urbanRide.custExceptions.ResourceNotFoundException;
import com.application.urbanRide.custExceptions.RuntimeConflictException;
import com.application.urbanRide.dtos.MailBodyDto;
import com.application.urbanRide.dtos.RideRequestDto;
import com.application.urbanRide.dtos.RiderDto;
import com.application.urbanRide.dtos.RiderRideDto;
import com.application.urbanRide.entities.*;
import com.application.urbanRide.entities.enums.RideRequestStatus;
import com.application.urbanRide.entities.enums.RideStatus;
import com.application.urbanRide.repositories.RideRequestRepository;
import com.application.urbanRide.repositories.RiderRepository;
import com.application.urbanRide.services.DriverService;
import com.application.urbanRide.services.EmailService;
import com.application.urbanRide.services.RideService;
import com.application.urbanRide.services.RiderService;
import com.application.urbanRide.strategies.StrategyManager;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RiderServiceImpl implements RiderService {

    private final ModelMapper modelMapper;
    private final RideRequestRepository rideRequestRepository;
    private final RiderRepository riderRepository;
    private final StrategyManager strategyManager;
    private final RideService rideService;
    private final DriverService driverService;
    private final EmailService emailService;

    @Override
    public RideRequestDto requestRide(RideRequestDto rideRequestDto) {
        RideRequest rideRequest = modelMapper.map(rideRequestDto,RideRequest.class);
        rideRequest.setStatus(RideRequestStatus.PENDING);
        Double fare = strategyManager.getRideFareCalculationStrategy().calculateFare(rideRequest);
        rideRequest.setFare(fare);
        Rider rider = getCurrentRider();
        rideRequest.setRider(rider);
        List<Driver> drivers = strategyManager
                                        .getDriverMatchingStrategy(getCurrentRider().getRating())
                                        .findMatchingDrivers(rideRequest);

        String[] driverEmails = drivers.stream().map(driver -> driver.getUser().getEmail()).toList().toArray(new String[0]);
        System.out.println(Arrays.toString(driverEmails));
        RideRequest savedRideRequest = rideRequestRepository.save(rideRequest);
        MailBodyDto mailBodyDto = emailService.prepareAcceptRideMail(savedRideRequest);
        emailService.sendEmail("thethijagjeet2000@gmail.com", mailBodyDto.getSubject(), mailBodyDto.getBody());
        return modelMapper.map(savedRideRequest,RideRequestDto.class);
    }

    @Override
    public String getRideRequestStatus(Long rideRequestId) {
        Rider rider = getCurrentRider();

        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId).orElseThrow(()->
                new ResourceNotFoundException("Invalid RideRequest id: "+rideRequestId));

        Ride ride = rideService.getRideByRideRequest(rideRequest);

        if(!rideRequest.getRider().equals(rider))
            throw new RuntimeConflictException("Cannot get status as current rider doesnot matches with Ride Request rider");

        if (rideRequest.getStatus().equals(RideRequestStatus.PENDING))
            return "Hold on! We are connecting with our drivers. We will update you once driver accepts this request! , Current status: "+rideRequest.getStatus();

        if (rideRequest.getStatus().equals(RideRequestStatus.CANCELLED))
            return "Ahh! This ride request has been cancelled!";

        if (rideRequest.getStatus().equals(RideRequestStatus.CONFIRMED) && ride.getRideStatus().equals(RideStatus.CONFIRMED))
            return "Your ride request has been confirmed. Please share OTP "+ride.getOtp()+ " with driver to start your ride.";

        else
            return "Your RideRequest status has been confirmed and ride has already been started!";
    }

    @Override
    public RideRequestDto cancelRideRequest(Long rideRequestId) {
        Rider rider = getCurrentRider();

        RideRequest rideRequest = rideRequestRepository.findById(rideRequestId).orElseThrow(()->
                 new ResourceNotFoundException("Invalid RideRequest Status: "+rideRequestId));

        if(!rideRequest.getRider().equals(rider))
            throw new RuntimeException("Cannot cancel RideRequest as current rider doesnot matches with RideRequest rider");

        if(!rideRequest.getStatus().equals(RideRequestStatus.PENDING))
        {
            throw new BadRequestException("RideRequest can only be cancelled if status is in pending, Current status: "+rideRequest.getStatus());
        }
        rideRequest.setStatus(RideRequestStatus.CANCELLED);
        return modelMapper.map(rideRequestRepository.save(rideRequest),RideRequestDto.class);
    }

    @Override
    @Transactional
    public RiderRideDto cancelRide(Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        Rider rider = getCurrentRider();

        if(!ride.getRider().equals(rider))
            throw new RuntimeException("Cannot cancel ride as Rider is different!");

        if(!ride.getRideStatus().equals(RideStatus.CONFIRMED))
            throw new RuntimeException("Ride cannot be cancelled invalid status, status:"+ride.getRideStatus());

        ride.setRideStatus(RideStatus.CANCELLED);
        Driver driver = ride.getDriver();
        driverService.setDriverAvailability(driver,true);
        return modelMapper.map(rideService.updateRideStatus(ride),RiderRideDto.class);
    }


    @Override
    public RiderDto getMyProfile() {

        return modelMapper.map(getCurrentRider(), RiderDto.class);
    }

    @Override
    public Page<RiderRideDto> getAllMyRides(PageRequest pageRequest) {
        return rideService.getAllRidesOfRider(getCurrentRider(),pageRequest).map(ride->
                modelMapper.map(ride,RiderRideDto.class));
    }

    @Override
    public Rider createRider(User user) {
        Rider rider = Rider.builder().user(user)
                .rating(0.0)
                .build();
        return riderRepository.save(rider);
    }

    @Override
    public Rider getCurrentRider() {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authenticatedUser.getPrincipal();
        return riderRepository.findByUser(user).orElseThrow(()-> new RuntimeException("Unable to load Profile!"));
    }

    @Override
    public void updateRiderRating(Rider rider,Double ratingValue) {
        rider.setRating(ratingValue);
        riderRepository.save(rider);
    }
}
