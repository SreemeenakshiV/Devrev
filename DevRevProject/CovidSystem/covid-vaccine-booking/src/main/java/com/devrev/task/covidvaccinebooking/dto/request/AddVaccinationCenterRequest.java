package com.devrev.task.covidvaccinebooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddVaccinationCenterRequest {
    private String name;
    private String location;
    private Long dosageAvailable;
}
