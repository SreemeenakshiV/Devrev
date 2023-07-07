package com.devrev.task.covidvaccinebooking.service;

import com.devrev.task.covidvaccinebooking.dto.request.AddVaccinationCenterRequest;
import com.devrev.task.covidvaccinebooking.entity.VaccinationCenter;
import com.devrev.task.covidvaccinebooking.repository.VaccinationCenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminService {
    private final VaccinationCenterRepository vaccinationCenterRepository;
    public ResponseEntity<?> addVaccinationCenter(AddVaccinationCenterRequest addVaccinationCenterRequest) {
        if (vaccinationCenterRepository.findByName(addVaccinationCenterRequest.getName()) == null) {
            vaccinationCenterRepository.save(VaccinationCenter.builder()
                    .name(addVaccinationCenterRequest.getName())
                    .location(addVaccinationCenterRequest.getLocation())
                    .dosageAvailable(addVaccinationCenterRequest.getDosageAvailable())
                    .build());
            return ResponseEntity.ok("Vaccination center added successfully");
        } else {
            return ResponseEntity.ok("Vaccination center name already exists");
        }
    }

    public ResponseEntity<?> getDosageDetails() {
        return ResponseEntity.ok(vaccinationCenterRepository.findAll());
    }

    public ResponseEntity<?> deleteVaccinationCenter(String vaccinationCenterName) {
        VaccinationCenter vaccinationCenter = vaccinationCenterRepository.findByName(vaccinationCenterName);
        if (vaccinationCenter == null) {
            return ResponseEntity.ok("Vaccination center name not available");
        } else {
            vaccinationCenterRepository.delete(vaccinationCenter);
            return ResponseEntity.ok("Vaccination center deleted successfully");
        }
    }
}
