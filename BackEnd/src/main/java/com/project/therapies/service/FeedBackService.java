package com.project.therapies.service;

import com.project.therapies.entity.Feedback;

import java.util.List;

public interface FeedBackService {

    Feedback giveFeedback(Feedback fb);

    List<Feedback> findAllFeedbacks();
}
