package com.wanted.preonboarding.service.repository;

import com.wanted.preonboarding.service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
