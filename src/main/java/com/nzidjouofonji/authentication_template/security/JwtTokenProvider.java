package com.nzidjouofonji.authentication_template.security;

import com.nzidjouofonji.authentication_template.exceptions.ExpiredJwtTokenException;
import com.nzidjouofonji.authentication_template.exceptions.InvalidJwtTokenException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.KeyPair;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMilliseconds}")
    private long jwtExpirationMilliseconds;

    @Value("${app.jwtRefreshExpirationMilliseconds}")
    private long jwtRefreshExpirationMilliseconds;

    private final TokenBlacklistRepository tokenBlacklistRepository;
    private final KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.RS256); // Asymmetric encryption

    public JwtTokenProvider(TokenBlacklistRepository tokenBlacklistRepository) {
        this.tokenBlacklistRepository = tokenBlacklistRepository;
    }

    // Common method to generate tokens (access or refresh)
    private String generateToken(String subject, Date now, Date expiryDate) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    // Generate Access Token (with roles)
    public String generateAccessToken(Authentication authentication) {
        UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMilliseconds);

        List<String> roles = userPrincipal.getAuthorities().stream()
                .map(authority -> "ROLE_" + authority.getAuthority()) // Spring Security convention
                .collect(Collectors.toList());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())  // Username or email
                .claim("roles", roles)                    // Add roles
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(keyPair.getPrivate(), SignatureAlgorithm.RS256)
                .compact();
    }

    // Generate Refresh Token (no roles)
    public String generateRefreshToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtRefreshExpirationMilliseconds);
        return generateToken(email, now, expiryDate);
    }

    // Extract claims from token
    private Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(keyPair.getPublic())  // RS256: use public key for validation
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException ex) {
            throw new InvalidJwtTokenException("Invalid JWT token", ex);
        }
    }

    // Get email/username from token
    public String getEmail(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    // Get roles from token
    public List<String> getRolesFromToken(String token) {
        return getClaimsFromToken(token).get("roles", List.class);
    }

    // Validate Access Token
    public boolean validateToken(String token) {
        try {
            if (isTokenRevoked(token)) {
                throw new InvalidJwtTokenException("JWT token has been revoked");
            }
            getClaimsFromToken(token); // Check token validity
            return true;
        } catch (ExpiredJwtException ex) {
            throw new ExpiredJwtTokenException("Expired JWT token", ex);
        } catch (MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new InvalidJwtTokenException("Invalid JWT token", ex);
        }
    }

    // Validate Refresh Token
    public boolean validateRefreshToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            return !isTokenRevoked(token) && claims.getExpiration().after(new Date());
        } catch (JwtException ex) {
            throw new InvalidJwtTokenException("Invalid Refresh Token", ex);
        }
    }

    // Check if token is revoked
    private boolean isTokenRevoked(String token) {
        return tokenBlacklistRepository != null && tokenBlacklistRepository.isTokenRevoked(token);
    }

    // Revoke a token
    public void revokeToken(String token) {
        if (tokenBlacklistRepository != null) {
            tokenBlacklistRepository.addTokenToBlacklist(token, getClaimsFromToken(token).getExpiration());
        }
    }
}
