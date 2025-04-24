package com.project.therapies.repository;

import com.project.therapies.entity.Therapy;
import com.project.therapies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);

}
