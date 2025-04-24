package com.project.therapies.service.Impl;

import com.project.therapies.entity.Therapy;
import com.project.therapies.repository.TherapyRepository;
import com.project.therapies.service.TherapyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TherapyServiceImpl implements TherapyService {

    @Autowired
    private TherapyRepository therapyRepository;

    @Override
    public Therapy addTherapy(Therapy therapy) {
        return therapyRepository.save(therapy);
    }

    @Override
    public Therapy updateTherapy(Long id, Therapy therapy) {
        Optional<Therapy> existingTherapy = therapyRepository.findById(id);
        if (existingTherapy.isPresent()) {
            Therapy updatedTherapy = existingTherapy.get();
            updatedTherapy.setName(therapy.getName());
            updatedTherapy.setDescription(therapy.getDescription());
            return therapyRepository.save(updatedTherapy);
        }
        return null;

    }

    @Override
    public boolean deleteTherapy(Long id) {
        if (therapyRepository.existsById(id)) {
            therapyRepository.deleteById(id);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public List<Therapy> getAllTherapies() {
        return therapyRepository.findAll();
    }

    @Override
    public List<Therapy> searchTherapies(String name) {
        return therapyRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Therapy findById(Long therapyId) {
        return null;
    }
}
