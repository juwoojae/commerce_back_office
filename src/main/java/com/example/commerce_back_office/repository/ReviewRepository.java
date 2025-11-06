package com.example.commerce_back_office.repository;

import com.example.commerce_back_office.domain.entity.Review;
import com.example.commerce_back_office.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    // 유저명, 상품명, 또는 리뷰에서 대소문자 구분 없이 특정 문자를 포함한 데이터 찾아서 반환
    List<Review> findByUser_NameIgnoreCaseContainingOrProduct_NameIgnoreCaseContainingOrContentIgnoreCaseContaining(String username, String product, String content);

    // 리뷰에서 특정 키워드로 검색
    default List<Review> searchKeyword(String keyword) {
        return findByUser_NameIgnoreCaseContainingOrProduct_NameIgnoreCaseContainingOrContentIgnoreCaseContaining(keyword,keyword,keyword);
    }

    Optional<Review> findByIdAndUser(Long reviewId, User user);

    List<Review> findAllByUser(User user);
}
