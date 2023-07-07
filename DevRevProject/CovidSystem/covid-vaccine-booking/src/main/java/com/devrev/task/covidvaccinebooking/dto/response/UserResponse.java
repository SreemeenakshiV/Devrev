package com.devrev.task.covidvaccinebooking.dto.response;

import com.devrev.task.covidvaccinebooking.constant.enums.UserType;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse{
    private Long id;
    private String name;
    private String email;
    private String address;
    private UserType userType;
}
