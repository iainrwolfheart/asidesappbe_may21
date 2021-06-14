package com.project.asidesappbe.jwt;

import com.project.asidesappbe.services.PlayerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtTokenUtil {

    private final PlayerService playerService;
    private final JwtConfig jwtConfig;

    public JwtTokenUtil(PlayerService playerService, JwtConfig jwtConfig) {
        this.playerService = playerService;
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

    protected boolean isValidToken(String token) {
        String username = getUsernameFromToken(token);
        return playerService.userExists(username) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date expirationDateTime = getExpirationDateTimeFromToken(token);
        return expirationDateTime.before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return extractSpecifiedClaim(token, Claims::getSubject);
    }

    private Date getExpirationDateTimeFromToken(String token) {
        return extractSpecifiedClaim(token, Claims::getExpiration);
    }

    /*
    Needs completing... How to extract custom claim..?
     */
//    private Set<SimpleGrantedAuthority> getUserGrantedAuthoritiesFromToken(String token) {
////        Call extractSpecifiedClaim w/token and claim resolver
//        return new HashSet<>();
//    }

    private <T> T extractSpecifiedClaim(String token, Function<Claims, T> claimsResolver) {
        Jws<Claims> jws = getAllClaimsFromToken(token);
        return claimsResolver.apply(jws.getBody());
    }

    /*
    More usable error handling needed here, probs
     */
    public Jws<Claims> getAllClaimsFromToken(String token) {
        Jws<Claims> parsedJwt = null;

        try {
            parsedJwt = Jwts.parserBuilder()
                    .setSigningKey(jwtConfig.getSecretKeySha())
                    .build()
                    .parseClaimsJws(token);
        } catch (JwtException jwtException) {
            System.out.println("Jwt parsing error: " + jwtException.getMessage());
        }
        return parsedJwt;
    }
}
