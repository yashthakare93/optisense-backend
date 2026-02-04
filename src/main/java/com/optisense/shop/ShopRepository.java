package com.optisense.shop;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopRepository extends JpaRepository<Shop, Long>{
    // Find shop by seller Id
    Optional<Shop> findBySellerId(Long sellerId);
    // Find shop by status
    List<Shop> findByStatus(ShopStatus status);
    // Find shop by status and order by created at
    List<Shop> findByStatusOrderByCreatedAtDesc(ShopStatus status);
    // Check if shop exists by seller Id
    boolean existsBySellerId(Long sellerId);
    // Count shops by status
    long countByStatus(ShopStatus status);
} 