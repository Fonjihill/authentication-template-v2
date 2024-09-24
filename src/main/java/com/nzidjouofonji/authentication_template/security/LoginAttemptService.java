package com.nzidjouofonji.authentication_template.security;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final int MAX_ATTEMPT = 5;
    private final long BLOCK_TIME = TimeUnit.MINUTES.toMillis(15); // Verrouillage de 15 minutes

    private Map<String, Integer> attempts = new HashMap<>();
    private Map<String, Long> blockTime = new HashMap<>();

    // Enregistre une tentative échouée
    public void loginFailed(String key) {
        int attemptsCount = attempts.getOrDefault(key, 0);
        attemptsCount++;
        attempts.put(key, attemptsCount);

        if (attemptsCount >= MAX_ATTEMPT) {
            blockTime.put(key, System.currentTimeMillis() + BLOCK_TIME);
        }
    }

    // Réinitialise les tentatives après un succès
    public void loginSucceeded(String key) {
        attempts.remove(key);
        blockTime.remove(key);
    }

    // Vérifie si le compte est bloqué
    public boolean isBlocked(String key) {
        if (blockTime.containsKey(key)) {
            if (blockTime.get(key) < System.currentTimeMillis()) {
                blockTime.remove(key);
                attempts.remove(key);
                return false;
            }
            return true;
        }
        return false;
    }
}
