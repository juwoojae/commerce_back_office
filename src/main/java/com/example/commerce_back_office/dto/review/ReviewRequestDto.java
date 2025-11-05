package com.example.commerce_back_office.dto.review;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {

    private final Integer rating;
    private final String content;


}
