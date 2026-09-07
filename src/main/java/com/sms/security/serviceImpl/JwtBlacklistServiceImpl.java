package com.sms.security.serviceImpl;


import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.sms.security.entity.JwtBlacklist;
import com.sms.security.repository.JwtBlacklistRepository;
import com.sms.security.service.JwtBlacklistService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class JwtBlacklistServiceImpl 
        implements JwtBlacklistService {


private final JwtBlacklistRepository repository;

@SuppressWarnings("null")
@Override
public void blacklistToken(String token){


    if(repository.existsByToken(token)){
        return;
    }


    JwtBlacklist blacklist =
            JwtBlacklist.builder()
            .token(token)
            .expiryDate(
                    LocalDateTime.now()
                    .plusMinutes(15)
            )
            .build();


    	repository.save(blacklist);

	}

	
	@Override
	public boolean isBlacklisted(String token){
	
	    return repository
	            .findByToken(token)
	            .filter(
	                    blacklist ->
	                    blacklist.getExpiryDate()
	                    .isAfter(
	                            LocalDateTime.now()
	                    )
	            )
	            .isPresent();
	
	}


}