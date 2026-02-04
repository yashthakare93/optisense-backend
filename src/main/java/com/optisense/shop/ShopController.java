package com.optisense.shop;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.optisense.common.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final JwtUtil jwtUtil;

    // 1. CREATE SHOP
    // POST /api/shop/create
    @PostMapping("/shop/create")
    public ResponseEntity<Shop> createShop(@RequestBody Shop shop, HttpServletRequest request) {
        Long sellerId = getCurrentUserId(request);
        shop.setSellerId(sellerId);
        Shop createdShop = shopService.createShop(shop);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdShop);
    }

    // 2. GET MY SHOP
    // GET /api/shop/my-shop
    @GetMapping("/shop/my-shop")
    public ResponseEntity<Shop> getMyShop(HttpServletRequest request) {
        Long sellerId = getCurrentUserId(request);
        Shop shop = shopService.getMyShop(sellerId);
        return ResponseEntity.ok(shop);
    }

    // 3. UPDATE MY SHOP
    // PUT /api/shop/update/{shopId}
    @PutMapping("/shop/update/{shopId}")
    public ResponseEntity<Shop> updateShop(@PathVariable Long shopId, @RequestBody Shop shop,
            HttpServletRequest request) {
        Long sellerId = getCurrentUserId(request);
        Shop updatedShop = shopService.updateShop(shopId, shop, sellerId);
        return ResponseEntity.ok(updatedShop);
    }

    // 4. GET ALL PENDING SHOPS (Admin)
    // GET /api/admin/shops/pending
    @GetMapping("/admin/shops/pending")
    public ResponseEntity<List<Shop>> getAllPendingShops(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            if (!jwtUtil.isAdmin(token)) {
                throw new RuntimeException("Access denied. Admin only.");
            }
        } else {
            throw new RuntimeException("No token provided");
        }

        List<Shop> pendingShop = shopService.getAllPendingShops();
        return ResponseEntity.ok(pendingShop);
    }

    // 5. APPROVE SHOP (Admin)
    // PUT /api/admin/shops/{shopId}/approve
    @PutMapping("/admin/shops/{shopId}/approve")
    public ResponseEntity<Shop> approveShop(@PathVariable Long shopId, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(!jwtUtil.isAdmin(token)){
                throw new RuntimeException("Access denied. Admin only.");
            }
        }else{
            throw new RuntimeException("No token provided");
        }
        Long adminId = getCurrentUserId(request);
        Shop approvedShop = shopService.approveShop(shopId, adminId);
        return ResponseEntity.ok(approvedShop);
    }

    // 6. REJECT SHOP (Admin)
    // PUT /api/admin/shops/{shopId}/reject
    @PutMapping("/admin/shops/{shopId}/reject")
    public ResponseEntity<Shop> rejectShop(@PathVariable Long shopId, @RequestBody String rejectionReason, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(!jwtUtil.isAdmin(token)){
                throw new RuntimeException("Access denied. Admin only.");
            }
        }else{
            throw new RuntimeException("No token provided");
        }
        Shop rejectedShop = shopService.rejectShop(shopId, rejectionReason);
        return ResponseEntity.ok(rejectedShop);
    }

    // 7. GET SHOPS BY STATUS (Admin)
    // GET /api/admin/shops/status/{status}
    @GetMapping("/admin/shops/status/{status}")
    public ResponseEntity<List<Shop>> getShopsByStatus(@PathVariable ShopStatus status, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(!jwtUtil.isAdmin(token)){
                throw new RuntimeException("Access denied. Admin only.");
            }
        }else{
            throw new RuntimeException("No token provided");
        }
        List<Shop> shops = shopService.getShopsByStatus(status);
        return ResponseEntity.ok(shops);
    }

    // 8. GET SHOP STATISTICS (Admin)
    // GET /api/admin/shops/stats
    @GetMapping("/admin/shops/stats")
    public ResponseEntity<ShopStats> getShopStats(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            token = token.substring(7);
            if(!jwtUtil.isAdmin(token)){
                throw new RuntimeException("Access denied. Admin only.");
            }
        }else{
            throw new RuntimeException("No token provided");
        }
        long pendingCount = shopService.getShopCountByStatus(ShopStatus.PENDING);
        long approvedCount = shopService.getShopCountByStatus(ShopStatus.APPROVED);
        long rejectedCount = shopService.getShopCountByStatus(ShopStatus.REJECTED);

        ShopStats stats = new ShopStats(pendingCount, approvedCount, rejectedCount);
        return ResponseEntity.ok(stats);
    }

    // Inner class for shop statistics response
    public static class ShopStats {
        public long pending;
        public long approved;
        public long rejected;

        public ShopStats(long pending, long approved, long rejected) {
            this.pending = pending;
            this.approved = approved;
            this.rejected = rejected;
        }
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            return jwtUtil.getUserIdFromToken(token);
        }
        return null;
    }
}