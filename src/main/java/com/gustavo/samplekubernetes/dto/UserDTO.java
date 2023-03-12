package com.gustavo.samplekubernetes.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserDTO(@Email(message = "Invalid e-mail format")
                      @NotNull(message = "E-mail is required") String email,
                      @NotNull(message = "First name is required") String firstName,
                      @NotNull(message = "Last name is required") String lastName,
                      @NotNull(message = "Password is required")
                      @Size(min = 8, message = "The password must have at least 8 characters") String password) {
}
