package com.backend.wear.service;

import com.backend.wear.dto.*;
import com.backend.wear.entity.Category;
import com.backend.wear.entity.Product;
import com.backend.wear.entity.User;
import com.backend.wear.entity.Wish;
import com.backend.wear.repository.CategoryRepository;
import com.backend.wear.repository.ProductRepository;
import com.backend.wear.repository.UserRepository;
import com.backend.wear.repository.WishRepository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final WishRepository wishRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    // ObjectMapper 생성
    ObjectMapper objectMapper = new ObjectMapper();

    /* // JSON 배열 파싱
     String[] array = objectMapper.readValue(jsonString, String[].class);
 */
    // List<String>를 JSON 문자열로 변환하는 메서드
    private String convertImageListToJson(List<String> imageList) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(imageList);
    }

    // JSON 문자열을 List<String>으로 변환하는 메서드
    private List<String> convertJsonToImageList(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, new TypeReference<List<String>>() {});
    }

    @Autowired
    public ProductService(ProductRepository productRepository, WishRepository wishRepository, UserRepository userRepository,CategoryRepository categoryRepository){
        this.productRepository=productRepository;
        this.wishRepository=wishRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
    }

    // 카테고리별, 최신순
    @Transactional
    public List<ProductResponseInnerDto.ScreenDto> findProductsByCategory (
            String categoryName, Long userId) throws Exception {

        // 카테고리와 일치하는 판매중인 상품 리스트 조회
        List<Product> productList = categoryName.equals("전체") ?
                productRepository.findAll() :
                productRepository.findProductsByCategoryName(categoryName);

        // 반환할 DTO 리스트 생성
        List <ProductResponseInnerDto.ScreenDto> productCategoryList = new ArrayList<>();

        for (Product product : productList) {
            // 상품 이미지 리스트 변환
            // JSON 배열 파싱
            String[] array = new String[0];
            try {
                array = objectMapper.readValue(product.getProductImage(), String[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // 사용자의 상품 찜 여부 확인
            boolean isSelected = wishRepository.findByUserIdAndProductId(userId, product.getId()).isPresent();

            // DTO 생성 및 리스트에 추가
            productCategoryList.add(ProductResponseInnerDto.ScreenDto.builder()
                    .id(product.getId())
                    .price(product.getPrice())
                    .productName(product.getProductName())
                    .productStatus(product.getProductStatus())
                    .postStatus(product.getPostStatus())
                    .productImage(array)
                    .isSelected(isSelected)
                    .time(ConvertTime.convertLocaldatetimeToTime(product.getCreatedAt()))
                    .build());
        }

        return productCategoryList;
    }

    //카테고리별, 판매중, 최신순
    @Transactional
    public List<ProductResponseInnerDto.ScreenDto> findProductsByCategoryOnSale(
            String categoryName,Long userId) throws Exception{

        // 카테고리와 일치하는 판매중인 상품 리스트 조회
        List<Product> productList = categoryName.equals("전체") ?
                productRepository.findAll() :
                productRepository.findProductByCategoryNameAndPostStatus(categoryName);

        // 반환할 DTO 리스트 생성
        List<ProductResponseInnerDto.ScreenDto> productCategoryOnSaleList = new ArrayList<>();

        for (Product product : productList) {
            // JSON 배열 파싱
            String[] array = new String[0];
            try {
                array = objectMapper.readValue(product.getProductImage(), String[].class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

            // 사용자의 상품 찜 여부 확인
            boolean isSelected = wishRepository.findByUserIdAndProductId(userId, product.getId()).isPresent();

            // DTO 생성 및 리스트에 추가
            productCategoryOnSaleList.add(ProductResponseInnerDto.ScreenDto.builder()
                    .id(product.getId())
                    .price(product.getPrice())
                    .productName(product.getProductName())
                    .productStatus(product.getProductStatus())
                    .postStatus(product.getPostStatus())
                    .productImage(array)
                    .isSelected(isSelected)
                    .time(ConvertTime.convertLocaldatetimeToTime(product.getCreatedAt()))
                    .build());
        }

        return productCategoryOnSaleList;
    }

//    // 카테고리별, 최신순
//    // 페이지네이션
//    @Transactional
//    public Page<ProductResponseDto> findProductsByCategory(String categoryName, Integer pageNumber ){
//        Page<Product> productsPage;
//
//        //카테고리가 전체 일 때
//        if (categoryName.equals("전체")){
//            productsPage = productRepository.findByIsPrivateFalse(pageRequest(pageNumber));
//
//            return productsPage.map(this::mapToProductResponseDto);
//        }
//
//        else{ //카테고리별
//            productsPage = productRepository.findByCategory_CategoryNameAndIsPrivateFalse(categoryName,pageRequest(pageNumber));
//
//            return productsPage.map(this::mapToProductResponseDto);
//        }
//    }
//
//    //카테고리별, 판매중, 최신순
//    @Transactional
//    public Page<ProductResponseDto> findProductsByCategoryOnSale(String categoryName, String postStatus, Integer pageNumber ){
//        Page<Product> productsPage;
//
//        //전체, 판매중, 최신순
//        if(categoryName.equals("전체")){
//            productsPage=productRepository
//                    .findByPostStatusAndIsPrivateFalse(postStatus,pageRequest(pageNumber));
//        }
//
//        //카테고리별 판매중 최신순
//        else{
//            productsPage =productRepository
//                    .findByPostStatusAndCategory_CategoryNameAndIsPrivateFalse(postStatus,categoryName,pageRequest(pageNumber));
//        }
//
//        return productsPage.map(this::mapToProductResponseDto);
//    }
//
//    private Pageable pageRequest(Integer pageNumber){
//        return PageRequest.of(pageNumber,12,
//                Sort.by("updatedAt").descending());
//    }

    // 상품 상세 조회
    @Transactional
    public ProductResponseInnerDto.DetailDto getProductPost(Long productId) throws Exception {
        Product product  = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "상품을 찾지 못하였습니다."));

        // 판매자
        User user=product.getUser();

        // 판매자 프로필 이미지 배열로 변환
        // JSON 배열 파싱
        String[] array = new String[0];
        try {
            array = objectMapper.readValue(user.getProfileImage(), String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        UserResponseInnerDto.SellerDto seller =  UserResponseInnerDto.SellerDto.builder()
                .id(user.getId())
                .nickName(user.getNickName())
                .profileImage(array)
                .level(user.getLevel().getLabel())
                .build();

        // 상품 이미지 배열로 변환
        String[] productArray = new String[0];
        try {
            productArray  = objectMapper.readValue(product.getProductImage(), String[].class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return ProductResponseInnerDto.DetailDto.builder()
                .id(product.getId())
                .seller(seller)
                .price(product.getPrice())
                .productName(product.getProductName())
                .productStatus(product.getProductStatus())
                .postStatus(product.getPostStatus())
                .productContent(product.getProductContent())
                .productImage(productArray)
                .place(product.getPlace())
                .createdTime(product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"))) // 수정
                .time(ConvertTime.convertLocaldatetimeToTime(product.getCreatedAt()))
                .build();
    }

    @Transactional
    public List<ProductResponseDto> searchProductByproductName(String searchName){
        List<Product> products  = productRepository.findByProductName(searchName);

        List<ProductResponseDto> responseDto = products.stream()
                .map(ProductResponseDto::new)
                .toList();

        return responseDto;
    }

    //상품 검색하기(productName 검색, 카테고리 검색)
    @Transactional
    public List<ProductResponseDto> searchProductByproductNameAndCategory(String searchName, String categoryName){

        List<Product> filteredProducts  = productRepository.findByProductNameAndCategoryName(searchName, categoryName);

        List<ProductResponseDto> responseDto = filteredProducts.stream()
                .map(ProductResponseDto::new)
                .toList();

        return responseDto;


    }

    //상품 등록하기
    @Transactional
    public void createProductPost(ProductPostRequestDto requestDTO, Long userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Category category = categoryRepository.findByCategoryName(requestDTO.getCategoryName());

        if (category == null) {
            throw new IllegalArgumentException("해당 카테고리를 찾지 못하였습니다.");
        }

        // List<String>을 JSON 문자열로 변환
        String productImageJson = convertImageListToJson(requestDTO.getProductImage());

        // 제공된 Product 객체의 데이터를 사용하여 새로운 Product 객체 생성
        Product newProduct = Product.builder()
                .productName(requestDTO.getProductName())
                .price(requestDTO.getPrice())
                .productImage(productImageJson) // JSON 문자열로 저장
                .productContent(requestDTO.getProductContent())
                .productStatus(requestDTO.getProductStatus())
                .place(requestDTO.getPlace())
                .user(user)
                .category(category)
                .build();

        productRepository.save(newProduct);
    }

    // 상품 정보 변경(전체를 받아서 전체를 변경)
    @Transactional
    public void updateProductPost(ProductPostRequestDto requestDTO, Long userId, Long productId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        // 기존 제품을 찾아 업데이트
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("제품을 찾지 못하였습니다."));

        Category category = categoryRepository.findByCategoryName(requestDTO.getCategoryName());

        if (category == null) {
            throw new IllegalArgumentException("해당 카테고리를 찾지 못하였습니다.");
        }

        // List<String>을 JSON 문자열로 변환
        String productImageJson = convertImageListToJson(requestDTO.getProductImage());

        // 업데이트된 정보로 기존 제품 업데이트
        product.setProductName(requestDTO.getProductName());
        product.setPrice(requestDTO.getPrice());
        product.setProductImage(productImageJson);
        product.setProductContent(requestDTO.getProductContent());
        product.setProductStatus(requestDTO.getProductStatus());
        product.setPlace(requestDTO.getPlace());
        product.setUser(user);
        product.setCategory(category);

        // updatedAt 업데이트
        product.setUpdatedAt(product.getUpdatedAt());

        productRepository.save(product);
    }

    // 상품 판매 상태 변경 (판매 완료 또는 판매 중)
    @Transactional
    public void updateProductPostStatus( ProductRequestDto requestDto ,Long userId ) throws Exception {

        System.out.println(requestDto);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        if(user != null){
            //해당 사용자의 상품을 제대로 요청했는지 확인
            Product product = productRepository.findById(requestDto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

            System.out.println(requestDto.getPostStatus());


            // 요청으로 받은 postStatus를 상품의 상태로 설정합니다.
            product.setPostStatus(requestDto.getPostStatus());

            // updatedAt 업데이트
            product.setUpdatedAt(product.getUpdatedAt());

            // 변경된 상품 정보를 저장합니다.
            productRepository.save(product);
        }
    }

    // 상품 숨기기 || 숨김 해제하기
    @Transactional
    public void updateProductPostPrivate( Long userId, Long productId ) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        if(user != null){
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

            if(product.isPrivate() == true){
                product.setPrivate(false);
            }else{
                product.setPrivate(true);
            }
            // updatedAt 업데이트
            product.setUpdatedAt(product.getUpdatedAt());
            productRepository.save(product);
        }
    }

    // 상품 삭제
    @Transactional
    public void deleteProductPost( Long userId, Long productId ) throws Exception {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        if(user != null){
            //해당 사용자의 상품을 제대로 요청했는지 확인
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

            productRepository.deleteByUserAndProduct(userId,productId);
        }
    }

    // 상품 찜하기
    @Transactional
    public void selectProduct(Long userId, Long productId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

        Wish wish = Wish.builder()
                .user(user)
                .product(product)
                .build();

        // 찜
        wishRepository.save(wish);
    }

    // 상품 찜 해제
    @Transactional
    public void deselectProduct(Long userId, Long productId) throws Exception{
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾지 못하였습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품을 찾지 못하였습니다."));

        Wish wish = wishRepository.findByUserIdAndProductId(userId,productId)
                .orElseThrow(() -> new IllegalArgumentException("사용자 아이디와 상품 아이디가 일치하는 찜한 상품 없음"));

        // 찜 해제
        wishRepository.delete(wish);
    }
}
