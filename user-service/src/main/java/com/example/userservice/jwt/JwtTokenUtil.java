package com.example.userservice.jwt;

import com.example.userservice.config.CustomUserDetails;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JwtTokenUtil {
    private final static String JWT_SECRET = "secret";

    public String generateToken(CustomUserDetails customUserDetails) {
        Date now = new Date();
        long JWT_EXPIRATION = 86400000L;
        Date expiredDate = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(customUserDetails.getCode())
                .setIssuedAt(now)
                .setExpiration(expiredDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String getUserCodeFromJwt(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }
}
