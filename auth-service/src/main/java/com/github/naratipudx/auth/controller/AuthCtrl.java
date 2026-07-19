package com.github.naratipudx.auth.controller;

import com.github.naratipudx.auth.dto.JwtResponse;
import com.github.naratipudx.auth.dto.LoginRequest;
import com.github.naratipudx.auth.dto.RefreshRequest;
import com.github.naratipudx.auth.exception.UnauthorizedException;
import com.github.naratipudx.auth.security.JwtTokenProvider;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthCtrl {

    private final JwtTokenProvider tokenProvider;

    public AuthCtrl(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest loginRequest) {
        // [Real system]: Retrieving data from the database to verify the password.
        if ("admin".equals(loginRequest.getUsername()) && "password".equals(loginRequest.getPassword())) {
            var userId = "user_999";
            var roles = List.of("USER", "ADMIN");
            var accessToken = tokenProvider.generateAccessToken(userId, roles);
            var refreshToken = tokenProvider.generateRefreshToken(userId);
            return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
        }

        throw new UnauthorizedException("Invalid credentials");
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@RequestBody RefreshRequest refreshRequest) {
        var refreshToken = refreshRequest.getRefreshToken();
        if (tokenProvider.validateRefreshToken(refreshToken)) {
            var userId = tokenProvider.getUserIdFromRefreshToken(refreshToken);

            // [Real system]: Retrieving the last access rights of this user from the database.
            var roles = List.of("USER", "ADMIN");
            var newAccessToken = tokenProvider.generateAccessToken(userId, roles);
            // Refresh Token Rotation
            var newRefreshToken = tokenProvider.generateRefreshToken(userId);
            return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken));
        }

        throw new UnauthorizedException("Invalid refresh token");
    }
}
