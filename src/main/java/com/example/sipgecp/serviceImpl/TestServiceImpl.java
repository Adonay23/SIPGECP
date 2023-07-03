package com.example.sipgecp.serviceImpl;

import com.example.sipgecp.entity.User;
import com.example.sipgecp.repository.UserRepository;
import com.example.sipgecp.responseDto.UserResponseDto;
import com.example.sipgecp.security.UserDetailsImpl;
import com.example.sipgecp.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class TestServiceImpl implements TestService {

	@Autowired
	UserRepository userRepository;
	@Override
	public String all() {
		return "Public Content.";
	}

	@Override
	public UserResponseDto user() {
		UserResponseDto user =new UserResponseDto();
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		User data= userRepository.findByUsername(userDetails.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User Not Found "));;

		user.setUsername(data.getUsername());
		user.setPassword(data.getPassword());
		user.setEmail(data.getEmail());

		return user;
	}

	@Override
	public String mod() {
		return "Moderator Board.";
	}

	@Override
	public String admin() {
		return "Admin Board.";
	}

}
