package com.example.commerce_back_office.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.commerce_back_office.domain.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}
