package com.nzidjouofonji.authentication_template.security;

import com.nzidjouofonji.authentication_template.models.RevokedToken;
import com.nzidjouofonji.authentication_template.repository.RevokedTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenBlacklistRepository {

    private final RevokedTokenRepository revokedTokenRepository;

    @Autowired
    public TokenBlacklistRepository(RevokedTokenRepository revokedTokenRepository) {
        this.revokedTokenRepository = revokedTokenRepository;
    }

    // Vérifie si un token est révoqué
    public boolean isTokenRevoked(String token) {
        // Cherche le token dans la base de données
        return revokedTokenRepository.findByToken(token).isPresent();
    }

    // Ajoute un token à la blacklist (révoque le token)
    public void addTokenToBlacklist(String token, Date expirationDate) {
        RevokedToken revokedToken = new RevokedToken(token, expirationDate);
        revokedTokenRepository.save(revokedToken);
    }

    // Nettoie les tokens expirés (facultatif, pour les supprimer après expiration)
    public void removeExpiredTokens() {
        Date now = new Date();
        revokedTokenRepository.deleteAll(
                revokedTokenRepository.findAll().stream()
                        .filter(token -> token.getExpirationDate().before(now))
                        .toList()
        );
    }
}
