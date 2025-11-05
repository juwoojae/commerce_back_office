package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.CustomUserDetails;
import com.example.commerce_back_office.dto.review.ReviewRequestDto;
import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(
            @AuthenticationPrincipal CustomUserDetails userPrincipal,
            @PathVariable int productId, @RequestBody ReviewRequestDto request) {

         ReviewResponseDto response = reviewService.save(userPrincipal.getUser(), productId, request);
        System.out.println("rating:"+response.getRating());
         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
