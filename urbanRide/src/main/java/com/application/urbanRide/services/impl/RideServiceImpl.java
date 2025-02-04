package com.application.urbanRide.services.impl;

import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.Ride;
import com.application.urbanRide.entities.RideRequest;
import com.application.urbanRide.entities.Rider;
import com.application.urbanRide.entities.enums.RideStatus;
import com.application.urbanRide.repositories.RideRepository;
import com.application.urbanRide.services.RideRequestService;
import com.application.urbanRide.services.RideService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class RideServiceImpl implements RideService {

    private final RideRepository rideRepository;
    private final ModelMapper modelMapper;
    private final RideRequestService rideRequestService;

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId).orElseThrow(()->
                new RuntimeException("Ride not found with id: "+rideId));
    }

    @Override
    public Ride getRideByRideRequest(RideRequest rideRequest) {
        return rideRepository.findByRideRequest(rideRequest).orElse(null);
    }

    @Override
    @Transactional
    public Ride createNewRide(RideRequest rideRequest, Driver driver) {
        Ride ride = modelMapper.map(rideRequest,Ride.class);
        ride.setDriver(driver);
        ride.setRideStatus(RideStatus.CONFIRMED);
        ride.setOtp(generateOtp());
        ride.setId(null);
        ride.setRideRequest(rideRequest);
        rideRequestService.updateRideRequest(rideRequest);
        return rideRepository.save(ride);
        }

    @Override
    public Ride updateRideStatus(Ride ride) {
        return rideRepository.save(ride);
    }

    @Override
    public Page<Ride> getAllRidesOfDriver(Driver driver, PageRequest pageRequest) {
        return rideRepository.findByDriver(driver,pageRequest);
    }

    @Override
    public Page<Ride> getAllRidesOfRider(Rider rider, PageRequest pageRequest) {
        return rideRepository.findByRider(rider,pageRequest);
    }

    public String generateOtp()
    {
        Random random = new Random();
        int otp = random.nextInt(10000);//gives 1 to 9999
        return String.format("%04d",otp);
    }

    public List<Ride> getAllRidesofDriver(Driver driver)
    {
        return rideRepository.findAllByDriver(driver);
    }

}
