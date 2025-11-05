package com.example.commerce_back_office.controller;

import com.example.commerce_back_office.dto.review.ReviewResponseDto;
import com.example.commerce_back_office.service.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    //keyword 전달시 - 리뷰 전체 조회 기능 구현
    //keyword null - 리뷰 검색 기능 적용
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getReviews(@RequestParam(required = false) String keyword) {
        List<ReviewResponseDto> response;

        // 검색할 키워드가 없다면
        if (!StringUtils.hasText(keyword)) {
            response = adminReviewService.getAll();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }

        response = adminReviewService.searchKeword(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //리뷰 상세 정보 조회 기능 구현
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getOneReview(@PathVariable Long id) {
        ReviewResponseDto response = adminReviewService.getOne(id);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //리뷰 삭제 기능 구현
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        adminReviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
