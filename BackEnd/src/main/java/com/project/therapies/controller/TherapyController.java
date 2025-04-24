package com.project.therapies.controller;


import com.project.therapies.entity.Therapy;
import com.project.therapies.repository.TherapyRepository;
import com.project.therapies.service.TherapyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/therapies")
public class TherapyController {
    Logger logger = LoggerFactory.getLogger(TherapyController.class);
    @Autowired
    private TherapyService therapyService;

    @PostMapping("/add-therapy")
    public ResponseEntity<?> addTherapy(@RequestBody Therapy therapy) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            logger.warn("Adding therapy: {}", therapy.getName());

            if (therapy.getName() == null || therapy.getDescription() == null) {
                response.put("msg", "Therapy name and description are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Therapy addedTherapy = therapyService.addTherapy(therapy);
            response.put("msg", "Therapy added successfully");
            response.put("therapyId", addedTherapy.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("msg", "An error occurred while adding therapy");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update-therapy/{id}")
    public ResponseEntity<Map<String, Object>> updateTherapy(@PathVariable Long id, @RequestBody Therapy therapy) {
        Map<String, Object> response = new HashMap<>();
        try {
            Therapy updatedTherapy = therapyService.updateTherapy(id, therapy);

            if (updatedTherapy == null) {
                response.put("msg","Therapy with ID " + id + " not found. Update failed.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            response.put("message", "Therapy updated successfully.");
            response.put("updatedTherapy", updatedTherapy);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            response.put("message", "An error occurred while updating the therapy: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


}
