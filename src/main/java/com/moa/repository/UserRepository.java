package com.moa.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

	Optional<User> findByUsername(String username);
}