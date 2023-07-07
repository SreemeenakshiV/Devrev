package com.devrev.task.covidvaccinebooking.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AvailableCenter {
    private String name;
    private String location;
    private long availableSlots;
}
