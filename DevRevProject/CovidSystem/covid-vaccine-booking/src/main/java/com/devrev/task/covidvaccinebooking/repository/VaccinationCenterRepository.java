package com.devrev.task.covidvaccinebooking.repository;

import com.devrev.task.covidvaccinebooking.entity.VaccinationCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VaccinationCenterRepository extends JpaRepository<VaccinationCenter, Long> {
    VaccinationCenter findByName(String name);
    List<VaccinationCenter> findByLocation(String location);


}
