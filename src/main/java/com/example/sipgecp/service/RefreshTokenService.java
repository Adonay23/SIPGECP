package com.example.sipgecp.service;

import java.time.Instant;

import java.util.Optional;
import java.util.UUID;

import com.example.sipgecp.Utils.TokenRefreshException;
import com.example.sipgecp.Utils.TokenUtils;
import com.example.sipgecp.entity.RefreshToken;
import com.example.sipgecp.entity.User;
import com.example.sipgecp.repository.RefreshTokenRepository;
import com.example.sipgecp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RefreshTokenService {
	@Value("${bezkoder.app.jwtRefreshExpirationMs}")
	private Long refreshTokenDurationMs;

	@Autowired
	private RefreshTokenRepository refreshTokenRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	TokenUtils jwtUtils;

	public Optional<RefreshToken> findByToken(String token) {
		return refreshTokenRepository.findByToken(token);
	}

	public RefreshToken createRefreshToken(Long userId) {
		RefreshToken refreshToken = new RefreshToken();

		User user=userRepository.findById(userId).get();


		refreshToken.setUser(user);
		refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
		refreshToken.setToken(jwtUtils.generateToken(user.getUsername()));

		refreshToken = refreshTokenRepository.save(refreshToken);
		return refreshToken;
	}

	public RefreshToken verifyExpiration(RefreshToken token) {
		if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepository.delete(token);
			throw new TokenRefreshException(token.getToken(),
					"Refresh token was expired. Please make a new signin request");
		}

		return token;
	}

	@Transactional
	public int deleteByUserId(Long userId) {
		return refreshTokenRepository.deleteByUser(userRepository.findById(userId).get());
	}

	public RefreshToken findByUserId(Long id) {
		return refreshTokenRepository.findByUser(userRepository.findById(id).get());
	}
}
