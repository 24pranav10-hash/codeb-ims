package com.codeb.ims.service;

import com.codeb.ims.dto.AuthDTOs.*;
import com.codeb.ims.entity.User;
import com.codeb.ims.repository.UserRepository;
import com.codeb.ims.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository  userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil         jwtUtil;
    private final JavaMailSender  mailSender;

    @Value("${app.base-url}")
    private String baseUrl;

    // ─────────────────────────────────────────────────────────────
    // REGISTER
    // ─────────────────────────────────────────────────────────────
    public MessageResponse register(RegisterRequest req) {

        // 1. Check duplicate email
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }

        // 2. Build user entity
        User user = new User();
        user.setFullName(req.getFullName());
        user.setEmail(req.getEmail());
        user.setPasswordHash(passwordEncoder.encode(req.getPassword()));
        user.setRole(User.Role.valueOf(req.getRole()));
        user.setEmailVerified(false);
        user.setVerificationToken(UUID.randomUUID().toString());

        userRepository.save(user);

        // 3. Send verification email
        sendVerificationEmail(user);

        return new MessageResponse(
                "Account created! Please check your email to verify your account."
        );
    }

    // ─────────────────────────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────────────────────────
    public LoginResponse login(LoginRequest req) {

        // 1. Find user by email
        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password."));

        // 2. Check password
        if (!passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password.");
        }

        // 3. Check account status
        if (user.getStatus() == User.Status.inactive) {
            throw new RuntimeException("Your account has been deactivated. Contact admin.");
        }

        // 4. Check email verified
        //if (!user.getEmailVerified()) {
          //  throw new RuntimeException("Please verify your email before logging in.");
        //}

        // 5. Generate JWT
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new LoginResponse(token, user.getEmail(), user.getFullName(), user.getRole().name());
    }

    // ─────────────────────────────────────────────────────────────
    // VERIFY EMAIL
    // ─────────────────────────────────────────────────────────────
    public MessageResponse verifyEmail(String token) {

        User user = userRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid or expired verification link."));

        user.setEmailVerified(true);
        user.setVerificationToken(null);   // clear token once used
        userRepository.save(user);

        return new MessageResponse("Email verified successfully! You can now log in.");
    }

    // ─────────────────────────────────────────────────────────────
    // FORGOT PASSWORD
    // ─────────────────────────────────────────────────────────────
    public MessageResponse forgotPassword(ForgotPasswordRequest req) {

        // Find user — don't reveal if email doesn't exist (security best practice)
        userRepository.findByEmail(req.getEmail()).ifPresent(user -> {
            String resetToken = UUID.randomUUID().toString();
            user.setResetToken(resetToken);
            user.setResetTokenExpiry(LocalDateTime.now().plusHours(1)); // 1-hour window
            userRepository.save(user);

            sendPasswordResetEmail(user, resetToken);
        });

        return new MessageResponse(
                "If that email exists, a password reset link has been sent."
        );
    }

    // ─────────────────────────────────────────────────────────────
    // RESET PASSWORD
    // ─────────────────────────────────────────────────────────────
    public MessageResponse resetPassword(ResetPasswordRequest req) {

        User user = userRepository.findByResetToken(req.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired reset token."));

        // Check token hasn't expired
        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired. Please request a new one.");
        }

        // Set new password
        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);

        return new MessageResponse("Password reset successfully! You can now log in.");
    }

    // ─────────────────────────────────────────────────────────────
    // PRIVATE HELPERS — Email sending
    // ─────────────────────────────────────────────────────────────
    private void sendVerificationEmail(User user) {
        String link = baseUrl + "/api/auth/verify-email?token=" + user.getVerificationToken();

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("CodeB IMS — Verify your email");
        mail.setText(
                "Hi " + user.getFullName() + ",\n\n" +
                        "Please verify your email by clicking the link below:\n" +
                        link + "\n\n" +
                        "This link does not expire.\n\n" +
                        "— CodeB IMS"
        );
        mailSender.send(mail);
    }

    private void sendPasswordResetEmail(User user, String resetToken) {
        String link = baseUrl + "/api/auth/reset-password?token=" + resetToken;

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(user.getEmail());
        mail.setSubject("CodeB IMS — Reset your password");
        mail.setText(
                "Hi " + user.getFullName() + ",\n\n" +
                        "Click the link below to reset your password (valid for 1 hour):\n" +
                        link + "\n\n" +
                        "If you did not request this, ignore this email.\n\n" +
                        "— CodeB IMS"
        );
        mailSender.send(mail);
    }
}
