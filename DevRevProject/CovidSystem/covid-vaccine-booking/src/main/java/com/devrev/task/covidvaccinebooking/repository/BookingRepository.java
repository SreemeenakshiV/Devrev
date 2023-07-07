package com.devrev.task.covidvaccinebooking.repository;

import com.devrev.task.covidvaccinebooking.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

}
