package com.example.commerce_back_office.repository;

import com.example.commerce_back_office.domain.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
