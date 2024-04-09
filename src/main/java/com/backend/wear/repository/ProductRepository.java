package com.backend.wear.repository;

import com.backend.wear.entity.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // 전체 상품 페이지 (숨김x)
    @Query("SELECT p FROM Product p WHERE p.isPrivate = false AND (:blockedUserIdList IS EMPTY OR p.user.id NOT IN :blockedUserIdList)")
    Page<Product> findAllProductPage(List<Long> blockedUserIdList, Pageable pageable );

    // 카테고리 이름과 일치하는 상품 페이지 (숨김x)
    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName AND p.isPrivate = false  AND (:blockedUserIdList IS EMPTY OR p.user.id NOT IN :blockedUserIdList)")
    Page<Product> findByCategoryNamePage(@Param("categoryName") String categoryName,List<Long> blockedUserIdList, Pageable pageable);

    // 전체 판매 중인 상품 페이지 (숨김x)
    @Query("SELECT p FROM Product p WHERE p.postStatus='onSale' AND p.isPrivate = false  AND (:blockedUserIdList IS EMPTY OR p.user.id NOT IN :blockedUserIdList)")
    Page<Product> findByPostStatusPage(List<Long> blockedUserIdList, Pageable pageable);

    // 카테고리 이름과 일치하는 판매 중인 상품 페이지 (숨김x)
    @Query("SELECT p FROM Product p WHERE p.category.categoryName = :categoryName AND p.postStatus='onSale' AND  p.isPrivate = false  AND (:blockedUserIdList IS EMPTY OR p.user.id NOT IN :blockedUserIdList)")
    Page<Product> findByCategoryNameAndPostStatusPage(@Param("categoryName") String categoryName, List<Long> blockedUserIdList, Pageable pageable);

    //검색어별 상품 조회
    /*List<Product> findByProductNameContainingIgnoreCase(String searchName);*/
    @Query("select p from Product p where p.productName LIKE %:searchName%")
    List<Product> findByProductName(String searchName);

    //검색어별 , 카테고리별 상품 조회
    @Query("select p from Product p where p.productName LIKE %:searchName% AND p.category.categoryName = :categoryName")
    List<Product> findByProductNameAndCategoryName(@Param("searchName") String seacrchName, @Param("categoryName") String categoryName);


    // 사용자 아이디로 상품 조회
    List<Product> findByUserId(Long userId);

    // 판매 상품 중 숨김 상품 조회
    @Modifying
    @Transactional
    @Query("SELECT p FROM Product p WHERE p.user.id = :userId AND p.isPrivate = true")
    List<Product> findByUserIdAndIsPrivateTrue(Long userId);

    //상품 삭제하기
    @Modifying
    @Transactional
    @Query("delete from Product p where p.user.id = :userId and p.id = :productId")
    void deleteByUserAndProduct(@Param("userId") Long userId, @Param("productId") Long productId);

    // 구매내역
  //  @Query("SELECT p FROM Product p WHERE p.user.id != :userId AND p.productStatus = 'soldOut'")
  //  List<Product> findSoldOutProductsByUserId(Long userId);
}