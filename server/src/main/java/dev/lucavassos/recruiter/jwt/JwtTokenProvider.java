package dev.lucavassos.recruiter.jwt;

import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication auth) {

        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        logger.info("Generating new token with principal {} and expiry date {}", userPrincipal, expiryDate);

        return Jwts.builder()
                .setId(Long.toString(userPrincipal.getId()))
                .setSubject(userPrincipal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token).getBody();

        return Long.parseLong(claims.getId());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            logger.error("Invalid JWT signature");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
            throw new UnauthorizedException("Invalid JWT token.");
        }
    }
}
