package com.example.sipgecp.repository;


import com.example.sipgecp.entity.RefreshToken;
import com.example.sipgecp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findByToken(String token);

	@Modifying
	int deleteByUser(User user);

	RefreshToken findByUser(User user);
}
