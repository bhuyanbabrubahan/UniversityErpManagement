package com.sms.security.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.sms.security.entity.JwtBlacklist;

public interface JwtBlacklistRepository extends JpaRepository<JwtBlacklist, Long> {

	boolean existsByToken(String token);

	Optional<JwtBlacklist> findByToken(String token);

	@Modifying
    @Transactional
    @Query("DELETE FROM JwtBlacklist j WHERE j.expiryDate < :date")
    void deleteByExpiryDateBefore(@Param("date") LocalDateTime date);
}