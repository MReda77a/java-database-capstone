package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class TokenService {

    private static final long SEVEN_DAYS_MS = 7L * 24 * 60 * 60 * 1000;

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    public TokenService(AdminRepository adminRepository,
                        DoctorRepository doctorRepository,
                        PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    // Builds the HMAC signing key from the configured secret
    public SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Generates a JWT whose subject is the user's email (or admin username), valid for 7 days
    public String generateToken(String identifier) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + SEVEN_DAYS_MS))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Reads the subject (email / username) from a token; returns null if invalid or expired
    public String extractIdentifier(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    // Role-based validation: the token must be genuine AND its subject must exist for that role.
    // role is one of "admin", "doctor", "patient".
    public boolean validateToken(String token, String role) {
        String identifier = extractIdentifier(token);
        if (identifier == null || role == null) {
            return false;
        }
        switch (role.toLowerCase()) {
            case "admin":
                return adminRepository.findByUsername(identifier) != null;
            case "doctor":
                return doctorRepository.findByEmail(identifier) != null;
            case "patient":
                return patientRepository.findByEmail(identifier) != null;
            default:
                return false;
        }
    }
}
