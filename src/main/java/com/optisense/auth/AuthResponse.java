package com.optisense.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private Long userId;
    private String name;
    private String email;
    private String password;
    private String role;
    private String token;
}
