package com.moa.user.dto;
import com.moa.entity.User.ApprovalStatus;
import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String name;
    private String email;
    private String phone;
    private String postcode;
    private String address;
    private String detailAddress;
    private String extraAddress;
    private ApprovalStatus artistApprovalStatus; // Enum 필드
}
