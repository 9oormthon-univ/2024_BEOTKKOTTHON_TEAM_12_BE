package com.backend.wear.repository;

import com.backend.wear.entity.Product;
import com.backend.wear.dto.ProductResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    //모든 상품 조회
    @Query("SELECT new com.backend.wear.dto.ProductResponseDto(p.id, p.price, p.productName, p.productStatus,p.wish,p.productImage) FROM Product p")
    Page<ProductResponseDto> findAllProducts(Pageable pageable);

    //카테고리별 조회
    Page<ProductResponseDto> findByCategory_CategoryName(String categoryName, Pageable pageable);

    //카테고리별 판매중 조회
    Page<ProductResponseDto> findByPostStatusAndCategory_CategoryName(String postStatus, String categoryName, Pageable pageable);

}