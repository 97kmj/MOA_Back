package com.moa.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.moa.entity.User;
import com.moa.entity.User.ApprovalStatus;

public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByProviderAndProviderId(String provider, String providerId);

    Optional<User> findByUsername(String username);
    
    //승인 여부로 유저리스트 
    List<User> findByArtistApprovalStatus(ApprovalStatus status);
    

}
