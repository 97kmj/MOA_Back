package com.moa.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

import com.moa.entity.User;
import com.moa.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository; 
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public String usernameByEmail(String email) throws Exception {
		return userRepository.findByEmail(email).orElseThrow(()->new Exception("이메일 오류")).getUsername();
	}
	
	public String usernameByPhone(String phone) throws Exception {
		return userRepository.findByPhone(phone).orElseThrow(()->new Exception("전화번호 오류")).getUsername();
	}

	public Boolean confirmUsernsem(String username) throws Exception {
		return userRepository.findByUsername(username).map(u->u.getUsername().equals(username)).orElse(false);
	}
	
	public void changePassword(String username, String password) throws Exception {
		User user = userRepository.findByUsername(username).orElseThrow(()->new Exception("아이디 오류"));
		user.setPassword(bCryptPasswordEncoder.encode(password));
		userRepository.save(user);
	}
}
