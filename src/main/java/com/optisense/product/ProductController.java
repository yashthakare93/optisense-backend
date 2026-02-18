package com.optisense.product;

import com.optisense.common.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class ProductController {

    private final ProductService productService;
    private final JwtUtil jwtUtil;

    // 1. Add Product
    @PostMapping(value = "/add", consumes = { "multipart/form-data" })
    public ResponseEntity<Product> addProduct(
            @RequestPart("product") Product product,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            HttpServletRequest request) {
        Long sellerId = getCurrentUserId(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.addProduct(product, images, sellerId));
    }

    // 2. Get All Products by Shop
    @GetMapping("/shop/{shopId}")
    public ResponseEntity<List<Product>> getProductsByShop(@PathVariable Long shopId) {
        return ResponseEntity.ok(productService.getProductsByShop(shopId));
    }

    // 3. Update Product
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product,
            HttpServletRequest request) {
        Long sellerId = getCurrentUserId(request);
        return ResponseEntity.ok(productService.updateProduct(id, product, sellerId));
    }

    // 4. Delete Product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, HttpServletRequest request) {
        Long sellerId = getCurrentUserId(request);
        productService.deleteProduct(id, sellerId);
        return ResponseEntity.ok().build();
    }

    // 5. Get All Products (Filtered)
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(
            @RequestParam(required = false) List<ProductCategory> categories,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) List<String> brands,
            @RequestParam(required = false) List<String> frameType,
            @RequestParam(required = false) List<String> frameColour,
            @RequestParam(required = false) List<String> frameMaterial,
            @RequestParam(required = false) List<String> Gender,
            @RequestParam(required = false) List<String> frameSize,
            @RequestParam(required = false) List<String> frameShape,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(
                productService.getAllProductsWithFilters(categories, minPrice, maxPrice, brands, frameType, frameColour,frameMaterial, Gender, frameSize, frameShape, search,
                        page, size));
    }

    // 6. Get Product by Id
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping("/variants/{modelNumber}")
    public ResponseEntity<List<Product>> getProductVariants(@PathVariable String modelNumber) {
        return ResponseEntity.ok(productService.getProductsByModelNumber(modelNumber));
    }

    private Long getCurrentUserId(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            return jwtUtil.getUserIdFromToken(token.substring(7));
        }
        return null;
    }
}
