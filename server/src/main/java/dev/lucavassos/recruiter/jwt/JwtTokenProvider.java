package dev.lucavassos.recruiter.jwt;

import dev.lucavassos.recruiter.auth.UserPrincipal;
import dev.lucavassos.recruiter.exception.UnauthorizedException;
import dev.lucavassos.recruiter.modules.user.entities.Role;
import dev.lucavassos.recruiter.modules.user.entities.RoleName;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger LOG = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${jwt.jwtSecret}")
    private String jwtSecret;

    @Value("${jwt.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication auth) {

        UserPrincipal userPrincipal = (UserPrincipal) auth.getPrincipal();

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
        Boolean isAdmin = userPrincipal.isAdmin();

        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", userPrincipal.getId());
        claims.put("sub", userPrincipal.getUsername());
        claims.put("isAdmin", isAdmin);


        LOG.info("Generating new token with principal {} and expiry date {}", userPrincipal, expiryDate);

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .setClaims(claims)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512).compact();
    }


    private Key getSigningKey() {
        byte[] keyBytes = this.jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Long getUserIdFromJWT(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey()).build()
                .parseClaimsJws(token).getBody().get("id", Long.class);
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            LOG.error("Invalid JWT signature");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (MalformedJwtException ex) {
            LOG.error("Invalid JWT token");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (ExpiredJwtException ex) {
            LOG.error("Expired JWT token");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (UnsupportedJwtException ex) {
            LOG.error("Unsupported JWT token");
            throw new UnauthorizedException("Invalid JWT token.");
        } catch (IllegalArgumentException ex) {
            LOG.error("JWT claims string is empty.");
            throw new UnauthorizedException("Invalid JWT token.");
        }
    }
}
