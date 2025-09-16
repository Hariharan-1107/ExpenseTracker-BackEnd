package hariharan.tech.expensetracker.services;

import hariharan.tech.expensetracker.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTService {
    private static final String SECRET = "super-secret-key-which-must-be-at-least-256-bits-long";
    private static final long EXPIRATION = 1000 * 60 * 120;

    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // Generate token
    public static String generateToken(String email) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)  // algorithm inferred from key
                .compact();
    }

    // Extract all claims with exception handling
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Generic method to extract claims with exception handling
    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimResolver.apply(claims);
        } catch (ExpiredJwtException e) {
            // For expired tokens, we can still extract claims from the expired token
            return claimResolver.apply(e.getClaims());
        }
    }

    // Extract subject (email/username) - handles expired tokens
    public  String extractSubject(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (JwtException e) {
            throw new JwtException("Invalid token: " + e.getMessage());
        }
    }

    // Check if token is expired - this is the key method that needs proper exception handling
    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractClaim(token, Claims::getExpiration);
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            // Token is already expired
            return true;
        } catch (JwtException e) {
            // Token is malformed or invalid - treat as expired
            return true;
        }
    }

    // Extract expiration date
    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        } catch (ExpiredJwtException e) {
            // Return the expiration date from the expired token
            return e.getClaims().getExpiration();
        } catch (JwtException e) {
            throw new JwtException("Cannot extract expiration from invalid token");
        }
    }

    // Validate token
    public boolean validateToken(String token, User user) {
        try {
            final String email = extractSubject(token);
            return email.equals(user.getEmail()) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }
}