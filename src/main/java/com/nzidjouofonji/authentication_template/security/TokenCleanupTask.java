package com.nzidjouofonji.authentication_template.security;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenCleanupTask {

    private final TokenBlacklistRepository tokenBlacklistRepository;

    public TokenCleanupTask(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    // Planifie la tâche tous les jours à minuit
    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupExpiredTokens() {
        tokenBlacklistRepository.removeExpiredTokens();
    }
}

