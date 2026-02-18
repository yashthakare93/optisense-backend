package com.optisense.product;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    @Override
    @Query("SELECT DISTINCT p FROM Product p")
    @EntityGraph(attributePaths = { "images", "specifications" })
    List<Product> findAll();

    @EntityGraph(attributePaths = { "images", "specifications" })
    @Query("SELECT DISTINCT p FROM Product p WHERE p.shopId = :shopId")
    List<Product> findByShopId(@Param("shopId") Long shopId);

    @EntityGraph(attributePaths = { "images", "specifications" })
    @Query("SELECT DISTINCT p FROM Product p WHERE p.shopId = :shopId AND p.category = :category")
    List<Product> findByShopIdAndCategory(@Param("shopId") Long shopId, @Param("category") ProductCategory category);

    @Query("SELECT p FROM Product p JOIN p.specifications s WHERE KEY(s) = 'Model No.' AND s = :modelNumber")
    List<Product> findByModelNumber(@Param("modelNumber") String modelNumber);

    @Override
    @EntityGraph(attributePaths = { "images", "specifications" })
    @Query("SELECT DISTINCT p FROM Product p")
    Page<Product> findAll(Specification<Product> spec, Pageable pageable);

}