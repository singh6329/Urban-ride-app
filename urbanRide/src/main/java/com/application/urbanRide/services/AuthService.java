package com.application.urbanRide.services;

import com.application.urbanRide.dtos.DriverDto;
import com.application.urbanRide.dtos.SignupDto;
import com.application.urbanRide.dtos.UserDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthService {

    String login(String email, String password, HttpServletResponse response);
    UserDto signup(SignupDto signupDto);
    DriverDto onboardDriver(Long userid,String vehicleId);
    String refreshToken(HttpServletRequest request);
    String logout(HttpServletRequest request,HttpServletResponse response);
}
