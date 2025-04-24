package com.project.therapies.service.Impl;

import com.project.therapies.DTO.FeedbackRequest;
import com.project.therapies.entity.Feedback;
import com.project.therapies.repository.FeedbackRepository;
import com.project.therapies.service.FeedBackService;
import com.project.therapies.service.TherapyService;
import com.project.therapies.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedBackServiceImpl implements FeedBackService {
    @Autowired
    private FeedbackRepository feedbackRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TherapyService therapyService;

    private FeedbackRequest feedbackRequest;

    @Override
    public Feedback giveFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public List<Feedback> findAllFeedbacks() {
        return feedbackRepository.findAll();
    }


}
