package com.wanted.preonboarding.repository;

import com.wanted.preonboarding.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
