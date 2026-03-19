package com.codeb.ims.controller;

import com.codeb.ims.dto.AuthDTOs.*;
import com.codeb.ims.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ── POST /api/auth/register ──────────────────────────────────
    // Body: { "fullName", "email", "password", "role" }
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        MessageResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // ── POST /api/auth/login ─────────────────────────────────────
    // Body: { "email", "password" }
    // Returns: { "token", "email", "fullName", "role" }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    // ── GET /api/auth/verify-email?token=... ─────────────────────
    // Called from the email link
    @GetMapping("/verify-email")
    public ResponseEntity<MessageResponse> verifyEmail(
            @RequestParam String token) {

        MessageResponse response = authService.verifyEmail(token);
        return ResponseEntity.ok(response);
    }

    // ── POST /api/auth/forgot-password ───────────────────────────
    // Body: { "email" }
    @PostMapping("/forgot-password")
    public ResponseEntity<MessageResponse> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        MessageResponse response = authService.forgotPassword(request);
        return ResponseEntity.ok(response);
    }

    // ── POST /api/auth/reset-password ────────────────────────────
    // Body: { "token", "newPassword" }
    @PostMapping("/reset-password")
    public ResponseEntity<MessageResponse> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        MessageResponse response = authService.resetPassword(request);
        return ResponseEntity.ok(response);
    }
}
