package com.example.sipgecp.serviceImpl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.sipgecp.Utils.TokenRefreshException;
import com.example.sipgecp.Utils.TokenUtils;
import com.example.sipgecp.entity.ERole;
import com.example.sipgecp.entity.RefreshToken;
import com.example.sipgecp.entity.Role;
import com.example.sipgecp.entity.User;
import com.example.sipgecp.repository.RoleRepository;
import com.example.sipgecp.repository.UserRepository;
import com.example.sipgecp.requestDto.LoginRequest;
import com.example.sipgecp.requestDto.SignupRequest;
import com.example.sipgecp.requestDto.TokenRefreshRequest;
import com.example.sipgecp.responseDto.JwtResponse;
import com.example.sipgecp.responseDto.MessageResponse;
import com.example.sipgecp.responseDto.TokenRefreshResponse;
import com.example.sipgecp.security.UserDetailsImpl;
import com.example.sipgecp.service.AuthService;
import com.example.sipgecp.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class AuthServiceImpl implements AuthService {

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	TokenUtils jwtUtils;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	RefreshTokenService refreshTokenService;

	@Override
	public ResponseEntity<?> Auth(LoginRequest loginRequest) {
		RefreshToken refreshToken = null;
				Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

		RefreshToken refreshtokenExist=refreshTokenService.findByUserId(userDetails.getId());

		if(refreshtokenExist!=null) {
			System.out.println("token exist");
			refreshTokenService.deleteByUserId(userDetails.getId());
			refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		}else{
			 refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
		}

	String jwt = jwtUtils.generateJwtToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(),
				userDetails.getUsername(), userDetails.getEmail()));

	}

	@Override
	public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {

		if (userRepository.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
		}

		if (userRepository.existsByEmail(signUpRequest.getEmail())) {
			return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
		}

		// Create new user's account
		User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(),
				encoder.encode(signUpRequest.getPassword()));

		Set<String> strRoles = signUpRequest.getRole();
		Set<Role> roles = new HashSet<>();

		if (strRoles == null) {
			Role userRole = roleRepository.findByName(ERole.ROLE_USER)
					.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
			roles.add(userRole);
		} else {
			strRoles.forEach(role -> {
				switch (role) {
				case "admin":
					Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(adminRole);

					break;
				case "mod":
					Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(modRole);

					break;
				default:
					Role userRole = roleRepository.findByName(ERole.ROLE_USER)
							.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
					roles.add(userRole);
				}
			});
		}

		user.setRoles(roles);
		userRepository.save(user);

		return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

	}

	@Override
	public ResponseEntity<?> refreshtoken(TokenRefreshRequest request) {
		String requestRefreshToken = request.getRefreshToken();

		return refreshTokenService.findByToken(requestRefreshToken).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String token = jwtUtils.generateToken(user.getUsername());
					return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
				})
				.orElseThrow(() -> new TokenRefreshException(requestRefreshToken, "Refresh token is not in database!"));

	}

}
