package com.application.urbanRide.services.impl;

import com.application.urbanRide.entities.User;
import com.application.urbanRide.repositories.UserRepository;
import com.application.urbanRide.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username).orElseThrow(()-> new BadCredentialsException("User does not exists!"));
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }
}
