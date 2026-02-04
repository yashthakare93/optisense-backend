package com.optisense.shop;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShopService { 

    private final ShopRepository shopRepository;

    // Create Shop
    public Shop createShop(Shop shop){
        // Check if shop already exists
        if(shopRepository.existsBySellerId(shop.getSellerId())){
            throw new RuntimeException("Shop already exists for this seller");
        }
        // Set initial status
        shop.setStatus(ShopStatus.PENDING);
        return shopRepository.save(shop);
    }

    // Get My Shop
    public Shop getMyShop(Long sellerId){
        return shopRepository.findBySellerId(sellerId)
            .orElseThrow(() -> new RuntimeException("Shop not found for this seller"));
    }

    // Update Shop
    public Shop updateShop(Long shopId, Shop updatedShop, Long sellerId){
        // Find existing shop by id
        Shop shop = shopRepository.findById(shopId)
            .orElseThrow(() -> new RuntimeException("Shop not found"));
        
        // Verify shop belongs to seller (FIXED: Using .equals() for Long comparison)
        if(!shop.getSellerId().equals(sellerId)){
            throw new RuntimeException("Unauthorized: You can only update your own shop");
        }
        
        // Check status is PENDING or REJECTED (only these can be edited)
        if(shop.getStatus() == ShopStatus.PENDING || shop.getStatus() == ShopStatus.REJECTED){
            // Update all fields
            shop.setShopName(updatedShop.getShopName());
            shop.setBusinessType(updatedShop.getBusinessType());
            shop.setPhone(updatedShop.getPhone());
            shop.setEmail(updatedShop.getEmail());
            shop.setShopAddress(updatedShop.getShopAddress());
            shop.setCity(updatedShop.getCity());
            shop.setState(updatedShop.getState());
            shop.setPinCode(updatedShop.getPinCode());
            shop.setCountry(updatedShop.getCountry());
            shop.setGstNumber(updatedShop.getGstNumber());
            shop.setAadhaarNumber(updatedShop.getAadhaarNumber());
            shop.setPanNumber(updatedShop.getPanNumber());
            shop.setDescription(updatedShop.getDescription());
            
            shop.setStatus(ShopStatus.PENDING);
            shop.setRejectionReason(null); 
            
            return shopRepository.save(shop);
        }
        
        throw new RuntimeException("Cannot update approved shop");
    }

    // Get all pending shops (Admin)
    public List<Shop> getAllPendingShops(){
        return shopRepository.findByStatusOrderByCreatedAtDesc(ShopStatus.PENDING);
    }

    // Get all shops by status (Admin)
    public List<Shop> getShopsByStatus(ShopStatus status){
        return shopRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    // Approve Shop (Admin only)
    public Shop approveShop(Long shopId, Long adminId){
        Shop shop = shopRepository.findById(shopId)
            .orElseThrow(() -> new RuntimeException("Shop not found"));
        
        if(shop.getStatus() != ShopStatus.PENDING){
            throw new RuntimeException("Only pending shops can be approved");
        }
        
        // Update status and track who approved
        shop.setStatus(ShopStatus.APPROVED);
        shop.setApprovedBy(adminId);
        shop.setApprovedAt(LocalDateTime.now());
        shop.setRejectionReason(null); 
        
        return shopRepository.save(shop);
    }

    // Reject Shop (Admin only)
    public Shop rejectShop(Long shopId, String rejectionReason){
        Shop shop = shopRepository.findById(shopId)
            .orElseThrow(() -> new RuntimeException("Shop not found"));
        
        if(shop.getStatus() != ShopStatus.PENDING){
            throw new RuntimeException("Only pending shops can be rejected");
        }
        
        // Validate rejection reason is provided
        if(rejectionReason == null || rejectionReason.trim().isEmpty()){
            throw new RuntimeException("Rejection reason is required");
        }
        
        shop.setStatus(ShopStatus.REJECTED);
        shop.setRejectionReason(rejectionReason);
        shop.setApprovedBy(null);
        shop.setApprovedAt(null);
        
        return shopRepository.save(shop);
    }

    // Get total shop count by status (Admin)
    public long getShopCountByStatus(ShopStatus status){
        return shopRepository.countByStatus(status);
    }
    
}
