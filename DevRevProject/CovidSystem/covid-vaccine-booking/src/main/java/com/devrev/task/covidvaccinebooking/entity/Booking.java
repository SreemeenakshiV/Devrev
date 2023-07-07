package com.devrev.task.covidvaccinebooking.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id")
    private Long userId;
    @Column(name = "created_date")
    private LocalDate createdDate;
    @Column(name = "vaccination_date")
    private LocalDate vaccinationDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "center_id")
    private VaccinationCenter vaccinationCenter;
}
