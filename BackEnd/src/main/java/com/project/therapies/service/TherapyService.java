package com.project.therapies.service;


import com.project.therapies.entity.Therapy;

import java.util.List;

public interface TherapyService {
    public Therapy addTherapy(Therapy therapy);
    Therapy updateTherapy(Long id, Therapy therapy);
    boolean deleteTherapy(Long id);
    List<Therapy> getAllTherapies();
    List<Therapy> searchTherapies(String name);
    Therapy findById(Long therapyId);
}
