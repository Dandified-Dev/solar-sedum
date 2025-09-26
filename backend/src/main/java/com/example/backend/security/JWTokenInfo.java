package com.example.backend.security;

import java.util.Date;
/**
 * Represents information extracted from a JSON Web Token (JWT).
 * This class is used to encapsulate details such as email, role, issued at, and expiration time.
 */
public class JWTokenInfo {

    public static final String KEY = "tokenInfo";

    private String email;
    private String role;
    private Date issuedAt;
    private Date expiration;

    public JWTokenInfo() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String isRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Date issuedAt) {
        this.issuedAt = issuedAt;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    @Override
    public String toString() {
        return "JWTokenInfo{" +
                "email='" + email + '\'' +
                ", role=" + role +
                ", issuedAt=" + issuedAt +
                ", expiration=" + expiration +
                '}';
    }
}

