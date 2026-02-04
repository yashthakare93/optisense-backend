package com.optisense.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.optisense.common.security.JwtUtil;
import com.optisense.user.User;
import com.optisense.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse signup(SignupRequest request) {
        if (userService.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .email(request.getEmail())
                .role(request.getRole())
                .build();

        user = userService.save(user);

        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();

    }

    public AuthResponse login(LoginRequest request) {
        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Credential!"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Credentails!");
        }

        String token = jwtUtil.generateToken(user.getUserId(), user.getEmail(), user.getRole());

        return AuthResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .token(token)
                .build();
    }
}
