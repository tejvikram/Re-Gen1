package com.project.therapies.repository;

import com.project.therapies.entity.Therapy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TherapyRepository extends JpaRepository<Therapy,Long> {
    List<Therapy> findByNameContainingIgnoreCase(String name);
}
