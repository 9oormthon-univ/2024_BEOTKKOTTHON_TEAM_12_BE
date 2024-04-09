package com.backend.wear.controller;

import com.backend.wear.dto.ProductPostRequestDto;
import com.backend.wear.dto.ProductRequestDto;
import com.backend.wear.dto.ProductResponseDto;
import com.backend.wear.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService){
        this.productService=productService;
    }

    //카테고리별 최신순 조회
//    api/products/category?categoryName={}&pageNumber={}
    @GetMapping("/category")
    public ResponseEntity<?> findAllProductsPage(@RequestParam String categoryName,@RequestParam Integer pageNumber)
    {
        System.out.println(categoryName);
        Page page = productService.findProductsByCategory(categoryName, pageNumber);

        //페이지에 요소가 있는 경우
        if(!page.isEmpty()){
            return ResponseEntity.ok(page);
        }

        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("카테고리와 일치하는 상품이 없습니다.");
        }
    }

//    카테고리별 최신순, 판매 상태
//    api/products/category/sale?categoryName={}&postStatus={}&pageNumber={}
    @GetMapping("/category/sale")
    public ResponseEntity<?> findProductsByCategoryName(@RequestParam String categoryName,
                                                        @RequestParam String postStatus, @RequestParam Integer pageNumber ){
        Page page=productService.findProductsByCategoryOnSale(categoryName,postStatus,pageNumber);

        //페이지에 요소가 있는 경우
        if(!page.isEmpty()){
            return ResponseEntity.ok(page);
        }

        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("카테고리와 일치하는 판매중인 상품이 없습니다.");
        }
    }


    //검색어 입력 후 검색어별, 최신순(default)으로 조회하기
    //  /products/search?searchName={searchName}

    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(@RequestParam String searchName) {

        try {
            List<ProductResponseDto> responseDto = productService.searchProductByproductName(searchName);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }


    //상품 리스트 검색어별, 카테고리별  , 최신순(default)으로 조회하기
    //   /products/search/category?searchName={searchName}?categoryName={categoryName}
    @GetMapping("/search/category")
    public ResponseEntity<?> searchProductsby(@RequestParam String searchName, String categoryName) {

        try {
            List<ProductResponseDto> responseDto = productService.searchProductByproductNameAndCategory(searchName, categoryName);
            return ResponseEntity.ok(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

    }




    //상품 상세 페이지 불러오기
    // api/products/{productId}
    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductPost(@PathVariable("productId") Long productId){
        ProductResponseDto productPost;
        try {
            productPost = productService.getProductPost(productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(productPost);
    }

    @PostMapping("/new/{userId}")
    public ResponseEntity<?> postProductPost(@PathVariable Long userId , @RequestBody @Valid ProductPostRequestDto requestDTO, Errors errors) throws Exception{
        try {
            productService.createProductPost(requestDTO,userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품상세/ 상품 정보 수정하기
    @PutMapping("/edit/{userId}/{productId}")
    public ResponseEntity<?> updateProductPost(@PathVariable Long userId , @PathVariable Long productId , @RequestBody @Valid ProductPostRequestDto requestDTO, Errors errors) throws Exception{
            productService.updateProductPost(requestDTO,userId,productId);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }


    //상품 상세/ 상품 판매 상태 변경하기
    @PutMapping("/soldOut/{userId}")
    public ResponseEntity<?> updateProductPostStatus(@PathVariable Long userId,@RequestBody @Valid ProductRequestDto requestDto ,Errors errors) throws Exception{
        try {
            productService.updateProductPostStatus(requestDto,userId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품 상세/ 상품 글 숨기기 || 숨김 해제하기
    @PutMapping("/private/{userId}/{productId}")
    public ResponseEntity<?> updateProductPostPrivate(@PathVariable Long userId , @PathVariable Long productId) throws Exception{
        try {
            System.out.println(userId);
            System.out.println(productId);
            productService.updateProductPostPrivate(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }

    //상품 상세/ 상품 삭제하기
    @DeleteMapping("/delete/{userId}/{productId}")
    public ResponseEntity<?> deleteProductPost(@PathVariable Long userId ,@PathVariable Long productId) throws Exception{
        try {
            productService.deleteProductPost(userId,productId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        return ResponseEntity.ok(HttpStatus.CREATED);

    }


}