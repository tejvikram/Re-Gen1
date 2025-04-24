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
@RequestMapping("api/users")
public class UserController {
    Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private FeedBackService feedBackService;

    @Autowired
    private UserService userService;

    @Autowired
    private TherapyService therapyService;

    @PostMapping("/message")
    public Map<String, String> msg(){
        return Map.of("msg","Connection established");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        HashMap<String, Object> response = new HashMap<>();
        try {
            logger.warn("{} is logged in",user.getUsername());
            if (user.getUsername() == null || user.getPassword() == null) {
                response.put("msg", "Username and Password fields are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            User registeredUser = userService.register(user);
            response.put("msg", "Registration successful");
            response.put("userId", registeredUser.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            response.put("msg", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("msg", "An error occurred during registration");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody HashMap<String, String> loginRequest){
        HashMap<String, Object> response = new HashMap<>();
        try {

            String username = loginRequest.get("username");
            String password = loginRequest.get("password");

            if (username == null || password == null) {
                response.put("msg", "Username and Password fields are required");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Optional<User> userOptional = userService.login(username, password);
            if (userOptional.isPresent()) {
                response.put("msg", "Login successful");
                response.put("userId", userOptional.get().getId());
                response.put("username",userOptional.get().getUsername());
                return ResponseEntity.ok(response);
            } else {
                response.put("msg", "Invalid username or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            response.put("msg", "An error occurred during login");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/view-therapies")
    public ResponseEntity<?> findAllTherapies(){
        try{
            logger.info("Fetching all therapies");
            List<Therapy> therapies = userService.findAllTherapies();
            return new ResponseEntity<>(therapies,HttpStatus.OK);
        }
        catch(Exception e){
            logger.error("Error fetching therapies: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fetch-user/{id}")
    public ResponseEntity<?> fetchUser(@PathVariable Long id){
        try{
            logger.warn("user id {} ",id);
            return new ResponseEntity<>(userService.getUserProfile(id),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search-therapy")
    public ResponseEntity<?> searchTherapies(@RequestParam String name){
        try{
            logger.info("Searching therapies of {}",name);
            List<Therapy> therapies =   therapyService.searchTherapies(name);

            if(therapies.isEmpty()){
                return new ResponseEntity<>(Map.of("msg","No therapies found"),HttpStatus.NOT_FOUND);
            }
            else{
                return new ResponseEntity<>(therapies,HttpStatus.OK);
            }
        }
        catch (Exception e){
            logger.error("Error searching therapies: {}",e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/feedback")
    public ResponseEntity<?> giveFeedback(@RequestBody Feedback fb) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            if (fb.getComment() == null || fb.getComment().isEmpty()) {
                response.put("msg", "Please provide a comment");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


            if (fb.getUser() == null || fb.getUser().getId() == null) {
                response.put("msg", "User information is missing or invalid");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }


            if (fb.getTherapy() == null || fb.getTherapy().getId() == null) {
                response.put("msg", "Therapy information is missing or invalid");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Feedback givenFeedback = feedBackService.giveFeedback(fb);
            response.put("msg", "Your feedback has been recorded successfully");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id){
        try{
            logger.info("Fetching profile of id {}",id);
            Optional<User> profile = userService.getUserProfile(id);
            if(profile.isEmpty()){
                return new ResponseEntity<>(Map.of("msg","Something went wrong"),HttpStatus.INTERNAL_SERVER_ERROR);
            }
            else{
                return new ResponseEntity<>(profile,HttpStatus.OK);
            }
        }
        catch(Exception e){
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
