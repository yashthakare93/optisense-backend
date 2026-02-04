package com.optisense.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupRequest {
    @NotBlank(message = "Name is Required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is Required")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    @NotBlank(message = "Password is Required")
    private String password;

    @NotBlank(message = "Role is Required")
    private String role;
}
