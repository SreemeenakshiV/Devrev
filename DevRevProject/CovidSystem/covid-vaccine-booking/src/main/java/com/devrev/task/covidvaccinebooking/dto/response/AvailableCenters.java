package com.devrev.task.covidvaccinebooking.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableCenters {
    List<AvailableCenter> availableCenters;
}
