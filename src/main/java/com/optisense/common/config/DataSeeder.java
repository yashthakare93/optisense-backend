package com.optisense.common.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.optisense.user.User;
import com.optisense.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Create default admin user if not exists
            if (!userService.existsByEmail("admin@optisense.com")) {
                User admin = User.builder()
                        .name("Admin User")
                        .email("admin@optisense.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role("ADMIN")
                        .build();

                userService.save(admin);
                log.info("✓ Default admin user created:");
                log.info("  Email: admin@optisense.com");
                log.info("  Password: admin123");
            } else {
                log.info("Admin user already exists");
            }

            // Create a test seller user if not exists
            if (!userService.existsByEmail("seller@optisense.com")) {
                User seller = User.builder()
                        .name("Test Seller")
                        .email("seller@optisense.com")
                        .password(passwordEncoder.encode("seller123"))
                        .role("SELLER")
                        .build();

                userService.save(seller);
                log.info("✓ Default seller user created:");
                log.info("  Email: seller@optisense.com");
                log.info("  Password: seller123");
            } else {
                log.info("Seller user already exists");
            }
        };
    }
}
