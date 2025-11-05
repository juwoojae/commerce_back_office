package com.example.commerce_back_office.dto.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {

    @NotNull(message = "가격은 필수 입력 항목입니다.")
    @Min(value = 1, message = "평점은 최소 1점이어야 합니다.")
    @Max(value = 5, message = "평점은 최대 5점까지 가능합니다.")
    private final Integer rating;
    @Size(min = 1, max = 200, message = "리뷰 내용은 1자 이상 200자 이하로 입력해주세요.")
    private final String content;


}
