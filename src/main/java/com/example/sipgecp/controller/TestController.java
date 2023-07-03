package com.example.sipgecp.controller;

import com.example.sipgecp.Utils.ServiceFactory;
import com.example.sipgecp.Utils.ServiceResponse;
import com.example.sipgecp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

	
	@Autowired
	TestService test;
	
	@GetMapping("/all")
	public String allAccess() {
		return test.all();
	}

	@GetMapping("/user")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<ServiceResponse> userAccess() {
				return ServiceFactory.createResponse(test.user());
	}

	@GetMapping("/mod")
	@PreAuthorize("hasRole('MODERATOR')")
	public String moderatorAccess() {
		return test.mod();
	}

	@GetMapping("/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public String adminAccess() {
		return test.admin();
	}
}
