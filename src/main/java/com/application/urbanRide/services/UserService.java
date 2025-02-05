package com.application.urbanRide.services;

import com.application.urbanRide.entities.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findById(Long userId);
}
