package com.optisense.admin;

import com.optisense.common.dto.ApiResponse;
import com.optisense.user.User;
import com.optisense.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<User>>> getAllUsers() {
        List<User> users = userService.findAll();
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<User>> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }
}
