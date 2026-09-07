package com.sms.security.scheduler;

import java.time.LocalDateTime;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sms.security.repository.JwtBlacklistRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtBlacklistCleanupScheduler {


    private final JwtBlacklistRepository repository;

    @Transactional
    @Scheduled(
        fixedRate = 3600000
    )
    public void cleanExpiredTokens(){


        repository.deleteByExpiryDateBefore(
                LocalDateTime.now()
        );


        log.info(
        	"Expired JWT blacklist tokens cleaned successfully"
        );

    }

}
