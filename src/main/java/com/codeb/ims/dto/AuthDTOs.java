package com.codeb.ims.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

// ── Inbound: Login ──────────────────────────────────────────
public class AuthDTOs {

    @Data
    public static class LoginRequest {
        @NotBlank @Email
        private String email;

        @NotBlank
        private String password;
    }

    // ── Inbound: Register ───────────────────────────────────────
    @Data
    public static class RegisterRequest {
        @NotBlank
        private String fullName;

        @NotBlank @Email
        private String email;

        @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
        private String password;

        // "admin" or "salesperson"
        private String role = "salesperson";
    }

    // ── Inbound: Forgot Password ────────────────────────────────
    @Data
    public static class ForgotPasswordRequest {
        @NotBlank @Email
        private String email;
    }

    // ── Inbound: Reset Password ─────────────────────────────────
    @Data
    public static class ResetPasswordRequest {
        @NotBlank
        private String token;

        @NotBlank @Size(min = 8)
        private String newPassword;
    }

    // ── Outbound: Login success ─────────────────────────────────
    @Data
    public static class LoginResponse {
        private String token;
        private String email;
        private String fullName;
        private String role;

        public LoginResponse(String token, String email, String fullName, String role) {
            this.token    = token;
            this.email    = email;
            this.fullName = fullName;
            this.role     = role;
        }
    }

    // ── Outbound: Generic message ───────────────────────────────
    @Data
    public static class MessageResponse {
        private String message;

        public MessageResponse(String message) {
            this.message = message;
        }
    }
}
