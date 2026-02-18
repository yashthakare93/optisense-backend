package com.optisense.product;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Data
@Table(name = "products_tbl")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private Long shopId;

    @NotBlank(message = "Product name is required")
    private String name;

    private String brand;

    @Column(length = 2000)
    private String description;

    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be positive")
    private Double price;

    @Min(value = 0, message = "Stock must be positive")
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Min(value = 0, message = "Discount must be between 0 and 100")
    private Integer discount;

    private Double discountedPrice;

    @jakarta.persistence.PrePersist
    @jakarta.persistence.PreUpdate
    public void prePersistOrUpdate() {
        if (price != null) {
            if (discount != null && discount > 0) {
                discountedPrice = price - (price * discount / 100.0);
            } else {
                discountedPrice = price;
            }
        }
    }

    // IMAGES - Using Set for @EntityGraph compatibility
    @ElementCollection
    @CollectionTable(name = "product_images_tbl", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private Set<String> images = new HashSet<>();

    // SPECIFICATIONS map
    @ElementCollection
    @CollectionTable(name = "product_specifications_tbl", joinColumns = @JoinColumn(name = "product_id"))
    @MapKeyColumn(name = "spec_key")
    @Column(name = "spec_value")
    private Map<String, String> specifications = new HashMap<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Get images sorted by numeric suffix
    public List<String> getSortedImages() {
        if (images == null || images.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> sortedList = new ArrayList<>(images);
        sortedList.sort((a, b) -> {
            int numA = extractNumber(a);
            int numB = extractNumber(b);

            if (numA != numB) {
                return numA - numB;
            }
            return a.compareToIgnoreCase(b);
        });
        return sortedList;
    }

    // Sort logic helper
    private int extractNumber(String s) {
        try {
            String name = s.replace("%20", " ");

            if (name.contains(".")) {
                name = name.substring(0, name.lastIndexOf('.'));
            }

            java.util.regex.Pattern p = java.util.regex.Pattern.compile("[ _-]([0-9]+)$");
            java.util.regex.Matcher m = p.matcher(name);

            if (m.find()) {
                return Integer.parseInt(m.group(1));
            }
        } catch (Exception e) {
            // Ignore
        }
        return Integer.MAX_VALUE;
    }
}
