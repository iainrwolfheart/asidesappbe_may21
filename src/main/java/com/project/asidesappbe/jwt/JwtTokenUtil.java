package com.project.asidesappbe.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDate;
import java.util.*;

public class JwtTokenUtil {

    private final JwtConfig jwtConfig;

    public JwtTokenUtil(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    protected String generateNewToken(Authentication authenticatedUser) {
        Map<String, Object> claims = new HashMap<>();
        return tokenGenerator(claims, authenticatedUser);
    }

    private String tokenGenerator(Map<String, Object> claims, Authentication authenticatedUser) {
//        Make a new token, obvs
        String token = Jwts.builder()
                .setSubject(authenticatedUser.getName())
                .claim("authorities", authenticatedUser.getAuthorities())
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date
                        .valueOf(LocalDate.now()
                                .plusDays(jwtConfig.getTokenValidityTimeInDays())))
                .signWith(jwtConfig.getSecretKeySha())
                .compact();
        return token;
    }

    protected boolean isValidToken(String tokenToValidate) {
//        Call getUsername
//        Check username matches in DB and token !expired
        return false;
    }

    private boolean isTokenExpired(String tokenToValidate) {
//        Call getExpirationTime
//        Check exp time against Now()
        return false;
    }

    private String getUsernameFromToken(String token) {
//        Call extractSpecifiedClaim w/token and claim resolver
        return "token";
    }

    private Date getExpirationDateFromToken(String token) {
//        Call extractSpecifiedClaim w/token and claim resolver
        return new Date();
    }

    private Set<SimpleGrantedAuthority> getUserGrantedAuthoritiesFromToken(String token) {
//        Call extractSpecifiedClaim w/token and claim resolver
        return new HashSet<>();
    }

//    private <T> T extractSpecifiedClaim(String token, Function<Claims, T> claimsResolver) {
//        Call getAllClaimsFromToken(token)
//        Extract claim using resolver and return
//    }

//    private Claims getAllClaimsFromToken(String token) {
//        return nothing;
//    }
}
