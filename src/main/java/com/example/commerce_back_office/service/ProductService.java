package com.example.commerce_back_office.service;

import com.example.commerce_back_office.domain.Category;
import com.example.commerce_back_office.domain.UserRole;
import com.example.commerce_back_office.domain.entity.Product;
import com.example.commerce_back_office.dto.product.*;
import com.example.commerce_back_office.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 1 상품 리스트 조회 + 검색 (유저, 관리자 모두 가능)
    public List<ProductListResponse> getProducts(String keyword) {
        List<Product> products;

        if (keyword == null || keyword.isBlank()) {
            products = productRepository.findAll();
        } else {
            Category category = null;
            try { category = Category.valueOf(keyword.toUpperCase()); } catch (Exception ignored) {}
            products = productRepository.findByNameContainingIgnoreCaseOrCategory(keyword, category);
            if (products.isEmpty()) throw new RuntimeException("검색 결과가 없습니다");
        }

        return products.stream().map(this::toProductListResponse).collect(Collectors.toList());
    }

    // 2 상품 상세 조회 (유저, 관리자 모두 가능)
    public ProductDetailResponse getProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));
        return toProductDetailResponse(product);
    }

    // 3 상품 등록 (관리자만)
    public ProductDetailResponse createProduct(ProductCreateRequest request, UserRole role) {
        if (role != UserRole.ADMIN) throw new RuntimeException("관리자 권한이 필요합니다.");
        if (request.getStock() == null || request.getStock() <= 0)
            throw new IllegalArgumentException("재고 수량은 0 이하로 설정할 수 없습니다.");

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

    // 4 상품 수정 (관리자만)
    public ProductDetailResponse updateProduct(Long id, ProductUpdateRequest request, UserRole role) {
        if (role != UserRole.ADMIN) throw new RuntimeException("관리자 권한이 필요합니다.");

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("상품을 찾을 수 없습니다."));

        if (request.getStock() != null && request.getStock() <= 0)
            throw new IllegalArgumentException("재고 수량은 0 이하로 수정할 수 없습니다.");

        if (request.getName() != null) product.setName(request.getName());
        if (request.getPrice() != null) product.setPrice(request.getPrice());
        if (request.getStock() != null) product.setStock(request.getStock());
        if (request.getCategory() != null) product.setCategory(request.getCategory());
        if (request.getDescription() != null) product.setDescription(request.getDescription());

        return toProductDetailResponse(product);
    }

    // DTO 변환
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

    private ProductDetailResponse toProductDetailResponse(Product product) {
        boolean lowStock = product.getStock() <= 5;
        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .description(product.getDescription())
                .createdDate(product.getCreatedDate())
                .lastModifiedDate(product.getLastModifiedDate())
                .lowStock(lowStock)
                .warningMessage(lowStock ? "재고가 5개 이하입니다. 재입고가 필요합니다." : null)
                .build();
    }
}








