package com.sharehome.member.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HexFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Password {

    @Column(nullable = false)
    private String hashedPassword;

    @Column(nullable = false)
    private String salt;

    public Password(String hashedPassword, String salt) {
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }

    public static Password hashPassword(String password) {
        String salt = createSalt();
        return new Password(hash(password, salt), salt);
    }

    private static String createSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[20];
        random.nextBytes(salt);
        return HexFormat.of().formatHex(salt);
    }

    private static String hash(String password, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((salt + password).getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    public boolean checkPassword(String plainPassword) {
        return this.hashedPassword.equals(hash(plainPassword, salt));
    }
}
