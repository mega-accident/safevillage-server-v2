package com.safevillage.safevillage.domain.auth.repository;

import com.safevillage.safevillage.domain.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserById(Long id);

  Optional<User> findByPhone(String phone);

  boolean existsByPhone(String phone);

  boolean existsByEmail(String email);
}
