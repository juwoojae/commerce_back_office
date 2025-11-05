package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.CommonResponse;
import com.example.commerce_back_office.dto.product.ProductCreateRequest;
import com.example.commerce_back_office.dto.product.ProductUpdateRequest;
import com.example.commerce_back_office.dto.product.ProductDetailResponse;
import com.example.commerce_back_office.dto.product.ProductListResponse;
import com.example.commerce_back_office.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.commerce_back_office.exception.code.SuccessCode.*;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 1 상품 리스트 조회 + 검색
     * - GET /products?keyword=검색어
     * - keyword가 없으면 전체 상품 조회
     * - keyword가 있으면 상품명 또는 카테고리 기준으로 검색
     */
    @GetMapping
    public ResponseEntity<CommonResponse<List<ProductListResponse>>> getProducts(
            @RequestParam(required = false) String keyword // 검색어 optional
    ) {
        List<ProductListResponse> products = productService.getProducts(keyword);
        return ResponseEntity.status(OK).body(CommonResponse.of(GET_PRODUCTS, products));
    }

    /**
     * 2 상품 상세 조회
     * - GET /products/{id}
     * - 특정 상품의 상세 정보를 반환
     * - 존재하지 않는 ID 요청 시 예외 발생
     */
    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<ProductDetailResponse>> getProduct(@PathVariable Long id) {

        ProductDetailResponse product = productService.getProduct(id);
        return ResponseEntity.status(OK).body(CommonResponse.of(GET_PRODUCT, product));
    }

    /**
     * 3 상품 등록
     * - POST /products
     * - RequestBody에 상품 정보(JSON) 전달
     * - 유효성 검사를 수행(@Valid)
     * - 등록 후 생성된 상품 정보를 반환
     */
    @PostMapping
    public ResponseEntity<CommonResponse<ProductDetailResponse>> createProduct(
            @Valid @RequestBody ProductCreateRequest request
    ) {
        ProductDetailResponse createdProduct = productService.createProduct(request);
        return ResponseEntity.status(CREATED).body(CommonResponse.of(CREATE_PRODUCT, createdProduct));
    }

    /**
     * 4 상품 수정
     * - PATCH /products/{id}
     * - RequestBody에 수정할 필드만 JSON으로 전달
     * - 재고 0 이하 입력 불가, 5 이하일 경우 경고 메시지 포함
     * - 수정 후 업데이트된 상품 정보를 반환
     */
    @PatchMapping("/{id}")

    public ResponseEntity<CommonResponse<ProductDetailResponse>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductUpdateRequest request
    ) {
        ProductDetailResponse updatedProduct = productService.updateProduct(id, request);
        return  ResponseEntity.status(OK).body(CommonResponse.of(UPDATE_PRODUCT, updatedProduct));
    }
}



