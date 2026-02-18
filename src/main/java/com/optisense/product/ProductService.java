package com.optisense.product;

import com.optisense.common.util.FileStorageUtil;
import com.optisense.shop.Shop;
import com.optisense.shop.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;
    private final FileStorageUtil fileStorageUtil;

    // 1. Add Product (With Image Upload)
    public Product addProduct(Product product, List<MultipartFile> imageFiles, Long sellerId) {
        // Verify Shop Ownership
        Shop shop = shopRepository.findById(product.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (!shop.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You do not own this shop");
        }

        // Process Images
        Set<String> imageUrls = new HashSet<>();
        if (imageFiles != null && !imageFiles.isEmpty()) {
            for (MultipartFile file : imageFiles) {
                if (!file.isEmpty()) {
                    String fileName = fileStorageUtil.storeFile(file);
                    // Construct the full URL or relative path
                    imageUrls.add("/uploads/" + fileName);
                }
            }
        }
        product.setImages(imageUrls);

        return productRepository.save(product);
    }

    // 2. Get All Products by Shop
    public List<Product> getProductsByShop(Long shopId) {
        return productRepository.findByShopId(shopId);
    }

    // 3. Get Product by Id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    public List<Product> getProductsByModelNumber(String modelNumber) {
        return productRepository.findByModelNumber(modelNumber);
    }

    // 4. Update Product (With Security Check)
    public Product updateProduct(Long id, Product updateProduct, Long sellerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Verify Shop Ownership
        Shop shop = shopRepository.findById(product.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (!shop.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You do not own this shop");
        }

        product.setName(updateProduct.getName());
        product.setPrice(updateProduct.getPrice());
        product.setStockQuantity(updateProduct.getStockQuantity());
        product.setDescription(updateProduct.getDescription());
        product.setCategory(updateProduct.getCategory());
        product.setBrand(updateProduct.getBrand());

        if (updateProduct.getImages() != null && !updateProduct.getImages().isEmpty()) {
            product.setImages(updateProduct.getImages());
        }
        if (updateProduct.getSpecifications() != null) {
            product.setSpecifications(updateProduct.getSpecifications());
        }
        return productRepository.save(product);
    }

    // 5. Delete Product (With Security Check)
    public void deleteProduct(Long id, Long sellerId) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Shop shop = shopRepository.findById(product.getShopId())
                .orElseThrow(() -> new RuntimeException("Shop not found"));

        if (!shop.getSellerId().equals(sellerId)) {
            throw new RuntimeException("Unauthorized: You do not own this shop");
        }

        productRepository.deleteById(id);
    }

    // 6. Get All Products with Filters
    public Page<Product> getAllProductsWithFilters(
            List<ProductCategory> categories,
            Double minPrice, Double maxPrice,
            List<String> brands,
            List<String> frameType,
            List<String> frameColour,
            List<String> frameMaterial,
            List<String> Gender,
            List<String> frameSize,
            List<String> frameShape,
            String search,
            int page,
            int size) {
        Specification<Product> spec = ProductSpecifications.withDynamicFilter(categories, minPrice, maxPrice, brands,
                frameType, frameColour, frameMaterial, Gender, frameSize, frameShape, search);
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(spec, pageable);
    }
}