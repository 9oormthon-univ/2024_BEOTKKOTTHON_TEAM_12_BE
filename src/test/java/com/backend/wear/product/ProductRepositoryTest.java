package com.backend.wear.product;

//import com.backend.wear.dto.product.ProductResponseDto;
//import com.backend.wear.entity.Product;
//import com.backend.wear.repository.ProductRepository;
//import com.backend.wear.service.product.ProductService;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
//
//import java.util.Collections;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//class ProductServiceTest {
//
//    @Mock
//    private ProductRepository productRepository;
//
//    //카테고리별 판매중 최신순
//    @Test
//    void findByProductStatusAndCategory_CategoryNameTest() {
//        // given
//        String productStatus = "onSale";
//        String categoryName = "testCategory";
//        Pageable pageable = Pageable.unpaged();
//        when(productRepository.findByPostStatusAndCategory_CategoryName(eq(categoryName),eq(productStatus), any())).thenReturn(new PageImpl<>(Collections.emptyList()));
//
//        // when
//        Page<Product> result = productRepository.findByPostStatusAndCategory_CategoryName(categoryName,productStatus, pageable);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(result.getContent()).isEmpty();
//
//        System.out.println("결과"+result.getContent());
//    }
//}