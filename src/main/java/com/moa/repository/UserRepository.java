package com.moa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.User;

public interface UserRepository extends JpaRepository<User, String> {

}
