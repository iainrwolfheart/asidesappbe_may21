package com.project.asidesappbe.jwt;

import com.project.asidesappbe.services.PlayerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
Current implementation parses JWT for each use case, e.g. isValid, getAuthorities.
May need more performant option...
 */
@Component
public class JwtTokenUtil {

    private final PlayerService playerService;
    private final JwtConfig jwtConfig;

    public JwtTokenUtil(PlayerService playerService, JwtConfig jwtConfig) {
        this.playerService = playerService;
        this.jwtConfig = jwtConfig;
    }

    protected String generateNewToken(Authentication authenticatedUser) {
        return tokenGenerator(authenticatedUser);
    }

    private String tokenGenerator(Authentication authenticatedUser) {
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

    private String getUsernameFromToken(String token) {
        return extractSpecifiedClaim(token, Claims::getSubject);
    }

    private Date getExpirationDateTimeFromToken(String token) {
        return extractSpecifiedClaim(token, Claims::getExpiration);
    }

    private <T> T extractSpecifiedClaim(String token, Function<Claims, T> claimsResolver) {
        Jws<Claims> jws = getAllClaimsFromToken(token);
        return claimsResolver.apply(jws.getBody());
    }

    /*
    Needs completing properly... How to extract custom claim w/same pattern..?
     */
    protected Set<SimpleGrantedAuthority> getUserGrantedAuthoritiesFromToken(String token) {
//        Call extractSpecifiedClaim w/token and claim resolver
        var authoritiesAsStringList = (List<Map<String, String>>) extractAuthoritiesClaim(token);
        System.out.println(authoritiesAsStringList.toString());
        Set<SimpleGrantedAuthority> grantedAuthorities = authoritiesAsStringList
                .stream()
                .map(entry -> new SimpleGrantedAuthority(entry.get("authority")))
                .collect(Collectors.toSet());
        return grantedAuthorities;
    }

    private Object extractAuthoritiesClaim(String token) {
        Claims claims = getAllClaimsFromToken(token).getBody();
        return claims.get("authorities");
    }

    /*
    Better error handling needed here, probs
     */
    private Jws<Claims> getAllClaimsFromToken(String token) throws JwtException {
        Jws<Claims> parsedJwt = null;

        parsedJwt = Jwts.parserBuilder()
                .setSigningKey(jwtConfig.getSecretKeySha())
                .build()
                .parseClaimsJws(token);
        if (parsedJwt == null) throw new JwtException("Exception getting claims from token: ");

        return parsedJwt;
    }
}

