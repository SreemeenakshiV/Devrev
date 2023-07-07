package com.devrev.task.covidvaccinebooking.controller;

import com.devrev.task.covidvaccinebooking.constant.enums.UserType;
import com.devrev.task.covidvaccinebooking.dto.request.AddVaccinationCenterRequest;
import com.devrev.task.covidvaccinebooking.dto.request.LoginRequest;
import com.devrev.task.covidvaccinebooking.dto.response.UserResponse;
import com.devrev.task.covidvaccinebooking.service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.devrev.task.covidvaccinebooking.constant.CommonConstants.INVALID_USER;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Value("${admin.id}")
    private Long id;
    @Value("${admin.name}")
    private String name;
    @Value("${admin.email}")
    private String email;
    @Value("${admin.password}")
    private String password;
    @Autowired
    private AdminService adminService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        //Since the task requirement doesn't ask for an admin sign up, I've hardcoded the credentials
        if (loginRequest.getEmail().equalsIgnoreCase(email) && loginRequest.getPassword().equals(password))
            return ResponseEntity.ok(UserResponse.builder()
                .id(id)
                .name(name)
                .email(email)
                .userType(UserType.ADMIN)
                .build());
        else
            return new ResponseEntity<>(INVALID_USER, HttpStatus.UNAUTHORIZED);
    }

    @PostMapping("/vaccination_center")
    public ResponseEntity<?> addVaccinationCenter(@RequestBody AddVaccinationCenterRequest addVaccinationCenterRequest) {
        return adminService.addVaccinationCenter(addVaccinationCenterRequest);
    }
    @GetMapping("/vaccination_center/dosage_details")
    public ResponseEntity<?> getDosageDetails() {
        return adminService.getDosageDetails();
    }
    @DeleteMapping("/vaccination_center/{name}")
    public ResponseEntity<?> addVaccinationCenter(@PathVariable String centerName) {
        return adminService.deleteVaccinationCenter(centerName);
    }


}
