package com.project.therapies.service.Impl;

import com.project.therapies.entity.Therapy;
import com.project.therapies.entity.User;
import com.project.therapies.repository.TherapyRepository;
import com.project.therapies.repository.UserRepository;
import com.project.therapies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TherapyRepository therapyRepository;

    @Override
    public User register(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> getUserProfile(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public List<Therapy> findAllTherapies() {
        return therapyRepository.findAll();
    }

    @Override
    public User findById(Long userId) {
        return null;
    }

}
