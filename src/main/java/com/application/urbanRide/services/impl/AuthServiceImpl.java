package com.application.urbanRide.services.impl;

import com.application.urbanRide.custExceptions.ResourceNotFoundException;
import com.application.urbanRide.custExceptions.RuntimeConflictException;
import com.application.urbanRide.dtos.DriverDto;
import com.application.urbanRide.dtos.PointDto;
import com.application.urbanRide.dtos.SignupDto;
import com.application.urbanRide.dtos.UserDto;
import com.application.urbanRide.entities.Driver;
import com.application.urbanRide.entities.User;
import com.application.urbanRide.entities.enums.Roles;
import com.application.urbanRide.repositories.UserRepository;
import com.application.urbanRide.security.JwtService;
import com.application.urbanRide.services.*;
import com.application.urbanRide.utils.GeometryUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final RiderService riderService;
    private final WalletService walletService;
    private final DriverService driverService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public String login(String email, String password, HttpServletResponse response) {
        Authentication authenticatedUser = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email,password));
        User user = (User) authenticatedUser.getPrincipal();
        String accessToken = jwtService.getAccessToken(user);
        String refreshToken = jwtService.getRefreshToken(user);
        Cookie cookie = new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
        return accessToken;
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        User user = modelMapper.map(signupDto, User.class);
        Boolean exists = userRepository.existsByEmail(user.getEmail());
        if(exists)
            throw new RuntimeConflictException("Email already Registered!");
        user.setRoles(Set.of(Roles.RIDER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        riderService.createRider(user);
        walletService.createNewWallet(savedUser);
        return modelMapper.map(savedUser,UserDto.class);
    }

    @Override
    @Transactional
    public DriverDto onboardDriver(Long userid,String vehicleId) {
        User user = userRepository.findById(userid).orElseThrow(()->
                new ResourceNotFoundException("User doesn't exists with id: "+userid));

        if (user.getRoles().contains(Roles.DRIVER))
            throw new RuntimeException("User with id: "+user.getId()+" is already a Driver");

        user.getRoles().add(Roles.DRIVER);
        userRepository.save(user);
        Double[] coordinates = {00.00,00.00};
        Driver createdDriver = Driver.builder()
                                    .available(false)
                                    .rating(0.0)
                                    .user(user)
                                    .vehicleId(vehicleId)
                                    .currentLocation(GeometryUtil.createPoint(new PointDto(coordinates))).build();
        Driver savedDriver = driverService.createNewDriver(createdDriver);
        return modelMapper.map(savedDriver,DriverDto.class);

    }

    @Override
    public String refreshToken(HttpServletRequest request) {
        Cookie refreshTokenCookie = Arrays.stream(request.getCookies()).filter(cookie ->
                cookie.getName().equals("refreshToken")).findFirst().orElseThrow(()-> new RuntimeException("Invalid session!"));
        String refreshToken = refreshTokenCookie.getValue();
        Long userId = jwtService.verifyToken(refreshToken);
        User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("Invalid Refresh token!"));
        return jwtService.getAccessToken(user);
    }

    @Override
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        if(request.getCookies()==null)
            throw new RuntimeException("Already Logged out!");
        Cookie cookie = Arrays.stream(request.getCookies()).filter(cookie1 -> cookie1.getName().equals("refreshToken")).findFirst().orElseThrow(()-> new RuntimeException("Already Logged out!"));
        if(cookie.getValue().isEmpty())
            throw new RuntimeException("Already Logged out!");
        cookie.setValue(null);
        response.addCookie(cookie);
        return "Logout Success!";
    }

}
