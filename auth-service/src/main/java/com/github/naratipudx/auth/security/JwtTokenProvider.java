package com.github.naratipudx.auth.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    @Value("${app.jwt.issuer}")
    private String issuer;

    @Value("${app.jwt.secret}")
    private String accessSecret;

    @Value("${app.jwt.refresh-secret}")
    private String refreshSecret;

    @Value("${app.jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${app.jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public String generateAccessToken(String userId, List<String> roles) {
        var now = new Date();
        var expiryDate = new Date(now.getTime() + accessTokenExpiration);
        return Jwts.builder()
            .subject(userId)
            .issuer(issuer) // Must match the key in kong.yml
            .claim("roles", String.join(",", roles))
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey(accessSecret), Jwts.SIG.HS256)
            .compact();
    }

    public String generateRefreshToken(String userId) {
        var now = new Date();
        var expiryDate = new Date(now.getTime() + refreshTokenExpiration);
        return Jwts.builder()
            .subject(userId)
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(getSigningKey(refreshSecret), Jwts.SIG.HS256)
            .compact();
    }

    public boolean validateRefreshToken(String token) {
        try {
            getClaimsJws(token);
            return true;
        } catch (Exception _) {
            return false;
        }
    }

    public String getUserIdFromRefreshToken(String token) {
        return getClaimsJws(token).getPayload().getSubject();
    }

    private SecretKey getSigningKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    private Jws<Claims> getClaimsJws(String token) {
        return Jwts.parser()
            .verifyWith(getSigningKey(refreshSecret))
            .build()
            .parseSignedClaims(token);
    }
}
