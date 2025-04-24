package com.project.therapies.repository;

import com.project.therapies.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback,Long> {

}
