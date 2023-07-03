package com.example.sipgecp.service;

import com.example.sipgecp.requestDto.LoginRequest;
import com.example.sipgecp.requestDto.SignupRequest;
import com.example.sipgecp.requestDto.TokenRefreshRequest;
import org.springframework.http.ResponseEntity;


public interface AuthService {
   

	public ResponseEntity<?> Auth(LoginRequest loginRequest);
	
	public ResponseEntity<?> registerUser (SignupRequest signUpRequest);
	
	public ResponseEntity<?> refreshtoken (TokenRefreshRequest request);
	
}
