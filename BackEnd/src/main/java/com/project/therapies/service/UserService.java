package com.project.therapies.service;

import com.project.therapies.entity.Therapy;
import com.project.therapies.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User register(User user);

    Optional<User> login(String username, String password);

    Optional<User> getUserProfile(Long userId);

    List<Therapy> findAllTherapies();

    User findById(Long userId);
}
