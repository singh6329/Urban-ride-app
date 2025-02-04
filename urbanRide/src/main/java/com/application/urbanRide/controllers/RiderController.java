package com.application.urbanRide.controllers;

import com.application.urbanRide.dtos.RatingDto;
import com.application.urbanRide.dtos.RideRequestDto;
import com.application.urbanRide.dtos.RiderDto;
import com.application.urbanRide.dtos.RiderRideDto;
import com.application.urbanRide.services.RatingService;
import com.application.urbanRide.services.RiderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/riders")
@PreAuthorize("hasAnyRole('RIDER')")
public class RiderController {

    private final RiderService riderService;
    private final int PAGE_SIZE = 10;
    private final RatingService ratingService;

    @GetMapping("/getMyProfile")
    public ResponseEntity<RiderDto> getMyProfile()
    {
        return new ResponseEntity<>(riderService.getMyProfile(), HttpStatus.OK);
    }

    @GetMapping("/getAllMyRides")
    public ResponseEntity<List<RiderRideDto>> getAllMyRides(@RequestParam(required = false,defaultValue = "0")int pageNumber)
    {
        return new ResponseEntity<>(riderService.getAllMyRides(PageRequest.of(pageNumber,PAGE_SIZE,
                Sort.by(Sort.Direction.DESC,"createdAt","id"))).getContent(),HttpStatus.OK);
    }

    @GetMapping("/getRideRequestStatus/{rideRequestId}")
    public ResponseEntity<String> getRideRequestStatus(@PathVariable(name = "rideRequestId") Long rideRequestId)
    {
        return new ResponseEntity<>(riderService.getRideRequestStatus(rideRequestId),HttpStatus.OK);
    }

    @PostMapping("/requestRide")
    public ResponseEntity<RideRequestDto> requestRide(@RequestBody RideRequestDto rideRequestDto)
    {
        return new ResponseEntity<>(riderService.requestRide(rideRequestDto),HttpStatus.OK);
    }

    @PostMapping("/cancelRideRequest/{rideRequestId}")
    public ResponseEntity<RideRequestDto> cancelRideRequest(@PathVariable(name = "rideRequestId") Long rideRequestId)
    {
        return new ResponseEntity<>(riderService.cancelRideRequest(rideRequestId),HttpStatus.OK);
    }

    @PostMapping("/cancelRide/{rideId}")
    public ResponseEntity<RiderRideDto> cancelRide(@PathVariable(name = "rideId") Long rideId)
    {
        return new ResponseEntity<>(riderService.cancelRide(rideId),HttpStatus.OK);
    }

    @PostMapping("/rateDriver/{rideId}")
    public ResponseEntity<String> rateDriver(@PathVariable(name = "rideId")Long rideId,
                                             @RequestBody RatingDto ratingDto)
    {
        return new ResponseEntity<>(ratingService.rateDriver(rideId,ratingDto.getRating()),HttpStatus.OK);
    }
}
