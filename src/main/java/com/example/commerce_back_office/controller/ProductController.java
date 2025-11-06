package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.dto.product.*;
import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 리스트 조회 + 검색 (유저/관리자 모두 가능)
    @GetMapping
    public ResponseEntity<List<ProductListResponse>> getProducts(@RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(productService.getProducts(keyword));
    }

    // 상품 상세 조회 (유저/관리자 모두 가능)
    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    // 상품 등록 (관리자만)
    @PostMapping
    public ResponseEntity<ProductDetailResponse> createProduct(
            @RequestBody ProductCreateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserRole role = userDetails.getUser().getRole();
        return ResponseEntity.ok(productService.createProduct(request, role));
    }

    // 상품 수정 (관리자만)
    @PatchMapping("/{id}")
    public ResponseEntity<ProductDetailResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UserRole role = userDetails.getUser().getRole();
        return ResponseEntity.ok(productService.updateProduct(id, request, role));
    }
}







