package com.optisense.shop;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "shops")
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shopId;

    @Column(nullable = false)
    private Long sellerId;

    // Basic Shop Information
    @NotBlank(message = "Shop name is required")
    @Size(min = 3, max = 255, message = "Shop name must be between 3 and 255 characters")
    @Column(nullable = false)
    private String shopName;

    @Size(max = 100)
    private String businessType;

    // Contact Information
    @NotBlank(message = "Phone is required")
    @Size(min = 10, max = 15, message = "Phone must be between 10 and 15 characters")
    private String phone;

    @Email(message = "Email should be valid")
    @Size(max = 255)
    private String email;

    // Address Information
    @NotBlank(message = "Address is required")
    @Size(min = 3, max = 500, message = "Address must be between 3 and 500 characters")
    @Column(length = 500)
    private String shopAddress;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 100)
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 100)
    private String state;

    @NotBlank(message = "Pin code is required")
    @Size(min = 6, max = 6, message = "Pin code must be 6 digits")
    private String pinCode;

    @Size(max = 100)
    private String country;

    // Legal Documents
    @Column(unique = true, length = 15)
    private String gstNumber;

    @Column(length = 12)
    private String aadhaarNumber;

    @Column(length = 10)
    private String panNumber;

    // Additional Information
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;

    // Status & Approval
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShopStatus status = ShopStatus.PENDING;

    @Column(length = 500)
    private String rejectionReason;

    @Column(nullable = true)
    private Long approvedBy;

    @Column(nullable = true)
    private LocalDateTime approvedAt;

    // Timestamps
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
