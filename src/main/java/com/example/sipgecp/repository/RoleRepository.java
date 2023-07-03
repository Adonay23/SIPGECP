package com.example.sipgecp.repository;


import com.example.sipgecp.entity.ERole;
import com.example.sipgecp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
	 Optional<Role> findByName(ERole name);
}
