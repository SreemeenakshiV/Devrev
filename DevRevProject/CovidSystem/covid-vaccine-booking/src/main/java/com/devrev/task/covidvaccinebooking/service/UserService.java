package com.devrev.task.covidvaccinebooking.service;

import com.devrev.task.covidvaccinebooking.constant.enums.UserType;
import com.devrev.task.covidvaccinebooking.dto.request.LoginRequest;
import com.devrev.task.covidvaccinebooking.dto.request.SignupRequest;
import com.devrev.task.covidvaccinebooking.dto.response.AvailableCenter;
import com.devrev.task.covidvaccinebooking.dto.response.AvailableCenters;
import com.devrev.task.covidvaccinebooking.dto.response.UserResponse;
import com.devrev.task.covidvaccinebooking.entity.Booking;
import com.devrev.task.covidvaccinebooking.entity.User;
import com.devrev.task.covidvaccinebooking.entity.VaccinationCenter;
import com.devrev.task.covidvaccinebooking.repository.BookingRepository;
import com.devrev.task.covidvaccinebooking.repository.UserRepository;
import com.devrev.task.covidvaccinebooking.repository.VaccinationCenterRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.devrev.task.covidvaccinebooking.constant.CommonConstants.*;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VaccinationCenterRepository vaccinationCenterRepository;
    private final BookingRepository bookingRepository;

    public ResponseEntity<?> signUp(SignupRequest signupRequest) {
        if (userRepository.findByEmailIgnoreCase(signupRequest.getEmail()) != null) {
            return ResponseEntity.ok("User email already present");
        } else {
            try {
                User user = User.builder()
                        .name(signupRequest.getName())
                        .password(passwordEncoder.encode(signupRequest.getPassword()))
                        .email(signupRequest.getEmail())
                        .address(signupRequest.getAddress())
                        .build();
                userRepository.save(user);
                return buildUserResponse(user);
            } catch (Exception e) {
                log.error("Unable to signup. Exception: ", e);
                return new ResponseEntity<>(COMMON_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        User user = userRepository.findByEmailIgnoreCase(loginRequest.getEmail());
        if (user == null) {
            return new ResponseEntity<>(INVALID_USER, HttpStatus.UNAUTHORIZED);
        } else {
            try {
                if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                    UserResponse userResponse = UserResponse.builder()
                            .name(user.getName())
                            .email(user.getEmail())
                            .address(user.getAddress())
                            .userType(UserType.USER)
                            .build();
                    return ResponseEntity.ok(userResponse);
                } else {
                    return new ResponseEntity<>(INVALID_USER, HttpStatus.UNAUTHORIZED);
                }
            } catch (Exception e) {
                log.error("Unable to signup. Exception: ", e);
                return new ResponseEntity<>(COMMON_ERROR_MSG, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    private ResponseEntity<?> buildUserResponse(User user) {
        if (user != null) {
            return ResponseEntity.ok(UserResponse.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .email(user.getEmail())
                    .address(user.getAddress())
                    .userType(UserType.USER)
                    .build());
        } else {
            return ResponseEntity.ok("Unable to find User");
        }
    }

    public ResponseEntity<?> searchVaccinationCenter(Long userId, String location, LocalDate date) {
        if (!isUserAvailable(userId))
            return new ResponseEntity<>(INVALID_USER_ID, HttpStatus.UNAUTHORIZED);
        List<VaccinationCenter> centerList = vaccinationCenterRepository.findByLocation(location);
        return availableCenters(centerList, date);
    }

    private ResponseEntity<?> availableCenters(List<VaccinationCenter> centerList, LocalDate date) {
        AvailableCenters availableCenters = new AvailableCenters();
        List<AvailableCenter> availableCenterList = new ArrayList<>();
        try {
            for (VaccinationCenter center : centerList) {
                AvailableCenter availableCenter = new AvailableCenter();
                availableCenter.setName(center.getName());
                availableCenter.setLocation(center.getLocation());
                availableCenter.setAvailableSlots(calculateAvailableSlots(center.getBookings()
                        , center.getDosageAvailable(), date));
                availableCenterList.add(availableCenter);
            }
            availableCenters.setAvailableCenters(availableCenterList);
            return ResponseEntity.ok(availableCenters);
        }catch (Exception e) {
            log.error("Unable to fetch centers", e);
            return ResponseEntity.internalServerError().body(COMMON_ERROR_MSG);
        }
    }

    private long calculateAvailableSlots(List<Booking> bookings, Long dosageAvailable, LocalDate date) {
        if (dosageAvailable == 0)
            return 0;
        long count = bookings.stream()
                .filter(booking -> booking.getVaccinationDate().equals(date))
                .count();
        if (count<10)
            return 10-count;
        else return 0;
    }

    public ResponseEntity<?> applyVaccination(Long userId, Long centerId, LocalDate date) {
        if (!isUserAvailable(userId))
            return new ResponseEntity<>(INVALID_USER_ID, HttpStatus.UNAUTHORIZED);
        Optional<VaccinationCenter> vaccinationCenterOptional = vaccinationCenterRepository.findById(centerId);
        if (vaccinationCenterOptional.isPresent()) {
            VaccinationCenter vaccinationCenter = vaccinationCenterOptional.get();
            if (calculateAvailableSlots(vaccinationCenter.getBookings()
                    , vaccinationCenter.getDosageAvailable(), date) > 0) {
                Booking booking = new Booking();
                booking.setUserId(userId);
                booking.setVaccinationDate(date);
                booking.setCreatedDate(LocalDate.now());
                booking.setVaccinationCenter(vaccinationCenter);
                bookingRepository.save(booking);
                List<Booking> bookings = vaccinationCenter.getBookings();
                bookings.add(booking);
                vaccinationCenter.setBookings(bookings);
                vaccinationCenter.setDosageAvailable(vaccinationCenter.getDosageAvailable()-1);
                vaccinationCenter.setDosageUsed(vaccinationCenter.getDosageUsed()+1);
                vaccinationCenterRepository.save(vaccinationCenter);
                return ResponseEntity.ok("Vaccination Applied Successfully. BookingID: " + booking.getId());
            } else {
                return ResponseEntity.ok("No available slots for center");
            }
        } else {
            return ResponseEntity.ok("Unable to find center");
        }
    }

    private boolean isUserAvailable(Long userId) {
        return userRepository.findById(userId).isPresent();
    }
}
