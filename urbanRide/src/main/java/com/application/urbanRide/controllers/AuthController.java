package com.application.urbanRide.controllers;

import com.application.urbanRide.dtos.*;
import com.application.urbanRide.services.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody SignupDto signupDto)
    {
        return new ResponseEntity<>(authService.signup(signupDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> signup(@RequestBody LoginDto loginDto, HttpServletResponse response)
    {
        return new ResponseEntity<>(authService.login(loginDto.getEmail(), loginDto.getPassword(), response), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/onboardNewDriver/{userId}")
    public ResponseEntity<DriverDto> onboardNewDriver(@PathVariable(name = "userId")Long userId,
                                                      @RequestBody OnboardDriverDto onboardDriverDto)
    {
        return new ResponseEntity<>(authService.onboardDriver(userId,onboardDriverDto.getVehicleId()),HttpStatus.CREATED);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refreshToken(HttpServletRequest request)
    {
        return new ResponseEntity<>(authService.refreshToken(request),HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request,HttpServletResponse response)
    {
        return new ResponseEntity<>(authService.logout(request,response),HttpStatus.OK);
    }
}
