package com.devrev.task.covidvaccinebooking.controller;

import com.devrev.task.covidvaccinebooking.dto.request.LoginRequest;
import com.devrev.task.covidvaccinebooking.dto.request.SignupRequest;
import com.devrev.task.covidvaccinebooking.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignupRequest signupRequest) {
        return userService.signUp(signupRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return userService.login(loginRequest);
    }

    @GetMapping("/search/vaccination_center")
    public ResponseEntity<?> searchVaccinationCenter(@RequestHeader Long userId,
                                                     @RequestParam String location, @RequestParam LocalDate date) {
        return userService.searchVaccinationCenter(userId, location, date);
    }

    @PostMapping("/vaccination/apply/{centerId}")
    public ResponseEntity<?> applyVaccination(@RequestHeader Long userId, @PathVariable Long centerId, @RequestParam LocalDate date) {
        return userService.applyVaccination(userId, centerId, date);
    }

}
