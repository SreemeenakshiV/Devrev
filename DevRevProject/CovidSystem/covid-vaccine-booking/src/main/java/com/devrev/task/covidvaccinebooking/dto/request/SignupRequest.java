package com.devrev.task.covidvaccinebooking.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    @NotBlank(message = "name cannot be null or empty")
    private String name;
    @NotBlank(message = "email cannot be null or empty")
    @Email
    private String email;
    @NotBlank(message = "password cannot be null or empty")
    private String password;
    @NotBlank(message = "address cannot be null or empty")
    private String address;
}
