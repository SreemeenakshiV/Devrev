package com.devrev.task.covidvaccinebooking.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "vaccination_centers")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VaccinationCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;
    @Column(unique = true)
    private String name;
    private String location;
    @Column(name = "dosage_available")
    private Long dosageAvailable;
    @Column(name = "dosage_used")
    private Long dosageUsed;
    @OneToMany(mappedBy = "vaccinationCenter", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

}
