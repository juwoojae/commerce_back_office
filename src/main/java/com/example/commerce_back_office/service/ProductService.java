package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.Category;
import com.example.commerce_back_office.domain.entity.Product;
import com.example.commerce_back_office.dto.product.*;
import com.example.commerce_back_office.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 1 상품 리스트 조회 + 검색 기능
    //  keyword가 null 또는 빈 문자열이면 전체 상품 조회
    //  keyword가 존재하면 이름 또는 카테고리 기준 부분 검색
    public List<ProductListResponse> getProducts(String keyword) {
        List<Product> products;

        if (keyword == null || keyword.isBlank()) {
            // 전체 상품 조회
            products = productRepository.findAll();
        } else {
            // keyword가 카테고리 Enum에 맞는 경우 변환
            Category category = null;
            try {
                category = Category.valueOf(keyword.toUpperCase());
            } catch (IllegalArgumentException ignored) {}

            // 이름 또는 카테고리 기준 검색
            products = productRepository.findByNameContainingIgnoreCaseOrCategory(keyword, category);
        }

        // 엔티티 → DTO 변환 후 반환
        return products.stream()
                .map(this::toProductListResponse)
                .collect(Collectors.toList());
    }

    // 2 특정 상품 상세 조회
    public ProductDetailResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        return toProductDetailResponse(product);
    }

    // 3 상품 등록 , 재고
    // 재고 수량 0 이하이면 IllegalArgumentException 발생
    public ProductDetailResponse createProduct(ProductCreateRequest request) {
        if (request.getStock() == null || request.getStock() <= 0) {
            throw new IllegalArgumentException("재고 수량은 0 이하로 설정할 수 없습니다.");
        }

        Product product = new Product(
                request.getName(),
                request.getPrice(),
                request.getStock(),
                request.getCategory(),
                request.getDescription()
        );

        productRepository.save(product);
        return toProductDetailResponse(product);
    }

    // 4 상품 정보 수정
    // 재고 0이하 입력 불가
    public ProductDetailResponse updateProduct(Long id, ProductUpdateRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (request.getStock() != null && request.getStock() <= 0) {
            throw new IllegalArgumentException("재고 수량은 0 이하로 수정할 수 없습니다.");
        }

        if (request.getName() != null) product.setName(request.getName());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStock() != null) product.setStock(request.getStock());
        if (request.getCategory() != null) product.setCategory(request.getCategory());
        if (request.getDescription() != null) product.setDescription(request.getDescription());

        return toProductDetailResponse(product);
    }

    // 5 엔티티 → 상품 리스트 DTO 변환
    // 재고가 5개 이하이면 lowStock 표시
    private ProductListResponse toProductListResponse(Product product) {
        boolean lowStock = product.getStock() <= 5;

        return ProductListResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .description(product.getDescription())
                .createdDate(product.getCreatedDate())
                .lowStock(lowStock)
                .build();
    }

    // 6 엔티티 → 상품 상세 DTO 변환
    // 재고가 5개 이하일 경우 경고 메시지 포함
    private ProductDetailResponse toProductDetailResponse(Product product) {
        boolean lowStock = product.getStock() <= 5;

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .createdDate(product.getCreatedDate())
                .lastModifiedDate(product.getLastModifiedDate())
                .lowStock(lowStock)
                .warningMessage(lowStock ? "재고가 5개 이하입니다. 재입고가 필요합니다." : null)
                .build();
    }
}





