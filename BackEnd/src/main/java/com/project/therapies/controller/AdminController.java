package com.project.therapies.controller;

import com.project.therapies.entity.Feedback;
import com.project.therapies.entity.Therapy;
import com.project.therapies.entity.User;
import com.project.therapies.service.FeedBackService;
import com.project.therapies.service.TherapyService;
import com.project.therapies.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private TherapyService therapyService;

    @Autowired
    private UserService userService;

    @Autowired
    private FeedBackService feedBackService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody HashMap<String, String> loginRequest) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            // Check for empty fields
            if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
                response.put("msg", "Username and Password fields are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Check hardcoded admin credentials
            if ("Teja".equals(username) && "teja123".equals(password)) {
                response.put("msg", "Login successful");
                response.put("userId", 0);
                response.put("username",username);
                return ResponseEntity.ok(response);
            }
            else {
                response.put("msg", "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("msg", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


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

    @DeleteMapping("/delete-therapy/{id}")
    public ResponseEntity<?> deleteTherapy(@PathVariable long id){
        try{
            logger.info("Deleting therapy with id {}",id);
            boolean deleted = therapyService.deleteTherapy(id);
            if(deleted){
                return new ResponseEntity<>(Map.of("msg","Therapy deleted successfully"),HttpStatus.OK);
            }
            else{
                return new ResponseEntity<>(Map.of("msg","Therapy not found"),HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/view-feedback")
    public ResponseEntity<?> findAllFeedbacks(){
        try{
            logger.info("Fetching all Feedbacks");
            List<Feedback> feedbacks = feedBackService.findAllFeedbacks();
            return new ResponseEntity<>(feedbacks,HttpStatus.OK);
        }
        catch(Exception e){
            logger.error("Error fetching feedbacks: {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate();
            }
            return new ResponseEntity<>(Map.of("msg","Logout successful"), HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
