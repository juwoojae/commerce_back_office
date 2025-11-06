package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.CommonResponse;
import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.dto.review.ReviewRequestDto;
import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.commerce_back_office.exception.code.SuccessCode.*;
import static org.springframework.http.HttpStatus.*;

/**
 * 리뷰 정보를 조회하는 REST 컨트롤러입니다.
 * POST: 리뷰 생성
 * GET: 전체 리뷰 목록 조회
 * GET: 특정 리뷰 상세 정보 조회
 */
@RestController
@RequestMapping("/products/{productId}/reviews" )
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<CommonResponse<ReviewResponseDto>> createReview(
            @AuthenticationPrincipal CustomUserDetails userPrincipal,
            @PathVariable long productId,
            @Valid @RequestBody ReviewRequestDto request) {

        ReviewResponseDto response = reviewService.save(userPrincipal.getUser(), productId, request);
        return ResponseEntity.status(CREATED).body(CommonResponse.of(CREATE_REVIEW, response));
    }

    @GetMapping("/{id}" )
    public ResponseEntity<CommonResponse<ReviewResponseDto>> getOneReview(
            @AuthenticationPrincipal CustomUserDetails userPrincipal,
            @PathVariable Long id) {
        ReviewResponseDto response = reviewService.getOne(id,userPrincipal.getUser());
        return ResponseEntity.status(OK).body(CommonResponse.of(GET_REVIEW, response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<ReviewResponseDto>>> getReviews(@AuthenticationPrincipal CustomUserDetails userPrincipal) {
        List<ReviewResponseDto> response = reviewService.getAll(userPrincipal.getUser());
        return ResponseEntity.status(OK).body(CommonResponse.of(GET_REVIEWS, response));
    }

    @PatchMapping("/{id}" )
    public ResponseEntity<CommonResponse<ReviewResponseDto>> patchReview(
            @AuthenticationPrincipal CustomUserDetails userPrincipal,
            @PathVariable Long id,
            @Valid @RequestBody ReviewRequestDto request) {
        ReviewResponseDto response = reviewService.patch(id, userPrincipal.getUser(), request);
        return ResponseEntity.status(OK).body(CommonResponse.of(UPDATE_REVIEW, response));
    }

    @DeleteMapping("/{id}" )
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal CustomUserDetails userPrincipal,
            @PathVariable Long id) {
        reviewService.delete(id, userPrincipal.getUser());
        return ResponseEntity.status(NO_CONTENT).build();
    }
}




