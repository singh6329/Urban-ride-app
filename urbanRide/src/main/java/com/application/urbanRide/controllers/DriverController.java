package com.application.urbanRide.controllers;

import com.application.urbanRide.dtos.DriverDto;
import com.application.urbanRide.dtos.DriverRideDto;
import com.application.urbanRide.dtos.RatingDto;
import com.application.urbanRide.dtos.RideStartDto;
import com.application.urbanRide.services.DriverService;
import com.application.urbanRide.services.RatingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drivers")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('DRIVER')")
public class DriverController {

    private final DriverService driverService;
    private final RatingService ratingService;
    private final int PAGE_SIZE = 10;

    @GetMapping("/getMyProfile")
    public ResponseEntity<DriverDto> getMyProfile()
    {
        return new ResponseEntity<>(driverService.getMyProfile(), HttpStatus.OK);
    }

    @GetMapping("getAllMyRides")
    public ResponseEntity<List<DriverRideDto>> getAllMyRides(@RequestParam(required = false,defaultValue = "0") int pageNumber)
    {
        return new ResponseEntity<>(driverService.getAllMyRides(PageRequest.of(pageNumber,PAGE_SIZE, Sort.by(Sort.Direction.DESC,"createdAt","id"))).getContent(),HttpStatus.OK);
    }

    @PostMapping("/acceptRide/{rideRequestId}")
    public ResponseEntity<DriverRideDto> acceptRide(@PathVariable(name = "rideRequestId") Long rideRequestId)
    {
       return new ResponseEntity<>(driverService.acceptRide(rideRequestId),HttpStatus.CREATED);
    }

    @PostMapping("/startRide/{rideId}")
    public ResponseEntity<DriverRideDto> startRide(@PathVariable(name = "rideId") Long rideId,
                                  @RequestBody RideStartDto rideStartDto)
    {
        return new ResponseEntity<>(driverService.startRide(rideId,rideStartDto.getOtp()),HttpStatus.CREATED);
    }

    @PostMapping("/endRide/{rideId}")
    public ResponseEntity<DriverRideDto> endRide(@PathVariable(name = "rideId") Long rideId)
    {
        return new ResponseEntity<>(driverService.endRide(rideId),HttpStatus.OK);
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<DriverRideDto> cancelRide(@PathVariable(name = "rideId") Long rideId)
    {
        return new ResponseEntity<>(driverService.cancelRide(rideId),HttpStatus.OK);
    }

    @PostMapping("/rateRider/{rideId}")
    public ResponseEntity<String> rateRider(@PathVariable(name = "rideId") Long rideId,
                              @RequestBody RatingDto ratingDto)
    {
        return new ResponseEntity<>(ratingService.rateRider(rideId,ratingDto.getRating()),HttpStatus.OK);
    }

    @PostMapping("/updateMyAvailability/{availability}")
    public ResponseEntity<DriverDto> updateDriverAvailability(@PathVariable(name = "availability")Boolean availability)
    {
        return new ResponseEntity<>(driverService.updateDriverAvailability(availability),HttpStatus.OK);
    }
}
