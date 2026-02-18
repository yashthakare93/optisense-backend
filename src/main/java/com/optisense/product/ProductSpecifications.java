package com.optisense.product;

import org.springframework.data.jpa.domain.Specification;
import java.util.List;

public class ProductSpecifications {

    public static Specification<Product> withDynamicFilter(
            List<ProductCategory> categories,
            Double minPrice,
            Double maxPrice,
            List<String> brands,
            List<String> frameType,
            List<String> frameColour,
            List<String> frameMaterial,
            List<String> gender,
            List<String> frameSize,
            List<String> frameShape,
            String search) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Specification<Product> spec = Specification.where(null);

            // 0. Global Search (Name, Brand, Description)
            if (search != null && !search.isEmpty()) {
                String pattern = "%" + search.toLowerCase() + "%";
                spec = spec.and((r, q, cb) -> cb.or(
                        cb.like(cb.lower(r.get("name")), pattern),
                        cb.like(cb.lower(r.get("brand")), pattern),
                        cb.like(cb.lower(r.get("description")), pattern)));
            }

            // 1. Filter by Categories
            if (categories != null && !categories.isEmpty()) {
                spec = spec.and((r, q, cb) -> r.get("category").in(categories));
            }

            // 2. Filter by Price Range
            if (minPrice != null) {
                spec = spec.and((r, q, cb) -> cb.greaterThanOrEqualTo(r.get("price"), minPrice));
            }
            if (maxPrice != null) {
                spec = spec.and((r, q, cb) -> cb.lessThanOrEqualTo(r.get("price"), maxPrice));
            }

            // 3. Filter by Brands
            if (brands != null && !brands.isEmpty()) {
                spec = spec.and((r, q, cb) -> r.get("brand").in(brands));
            }

            // 4. Filter by Frame Types (Stored in specifications map)
            if (frameType != null && !frameType.isEmpty()) {
                spec = spec.and((r, q, cb) -> {
                    var specsJoin = r.joinMap("specifications");
                    return cb.and(
                            cb.equal(specsJoin.key(), "Frame Type"),
                            specsJoin.value().in(frameType));
                });
            }

            // 5. Filter by Frame Colour (Stored in specifications map)
            if (frameColour != null && !frameColour.isEmpty()) {
                spec = spec.and((r, q, cb) -> {
                    var specsJoin = r.joinMap("specifications");
                    return cb.and(
                            cb.equal(specsJoin.key(), "Frame Colour"),
                            specsJoin.value().in(frameColour));
                });
            }

            // 6. Filter by Frame Material (Stored in specifications map)
            if (frameMaterial != null && !frameMaterial.isEmpty()) {
                spec = spec.and((r, q, cb) -> {
                    var specsJoin = r.joinMap("specifications");
                    return cb.and(
                            cb.equal(specsJoin.key(), "Frame Material"),
                            specsJoin.value().in(frameMaterial));
                });
            }

            // 7. Filter by Gender (Stored in specifications map)
            if (gender != null && !gender.isEmpty()) {
                spec = spec.and((r, q, cb) -> {
                    var specsJoin = r.joinMap("specifications");
                    return cb.and(
                            cb.equal(specsJoin.key(), "Gender"),
                            specsJoin.value().in(gender));
                });
            }

            // 8. Filter by Frame Size (Stored in specifications map)
            if (frameSize != null && !frameSize.isEmpty()) {
                spec = spec.and((r, q, cb) -> {
                    var specsJoin = r.joinMap("specifications");
                    return cb.and(
                            cb.equal(specsJoin.key(), "Frame Size"),
                            specsJoin.value().in(frameSize));
                });
            }

            // 9. Filter by Frame Shape (Stored in specifications map)
            if (frameShape != null && !frameShape.isEmpty()) {
                spec = spec.and((r, q, cb) -> {
                    var specsJoin = r.joinMap("specifications");
                    return cb.and(
                            cb.equal(specsJoin.key(), "Frame Shape"),
                            specsJoin.value().in(frameShape));
                });
            }

            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }
}
