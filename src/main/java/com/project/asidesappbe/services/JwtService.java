//package com.project.asidesappbe.services;
//
//import com.project.asidesappbe.models.Player;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import io.jsonwebtoken.security.Keys;
//import org.springframework.stereotype.Service;
//
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class JwtService {
//
//    //    NEEDS REPLACING IRL?
//    private Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
//
//    public String generateToken (Player userDetails) {
//        Map<String, Object> claims = new HashMap<>();
//        return createToken(claims, userDetails.getUsername());
//    }
//
//    private String createToken(Map<String, Object> claims, String username) {
////        IF I'M UNDERSTANDING IETF.ORG CORRECTLY, IT MIGHT BE WORTH CHANGING SETSUBJECT TO USERID,
////        ADDING A 'ISS' (ISSUER) AND KEEPING A HIDDEN VALUE TO SIGNIFY ISSUER, AND SIMILARLY AN 'AUD'
////        (AUDIENCE) WHICH IS KNOWN TO-/HIDDEN IN FRONT END..?
//
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(username)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) //CHANGE THIS AS RQRD
//                .signWith(secretKey)
//                .compact();
//    }
//
//    public boolean validateToken(String token, Player userDetails) {
//        final String username = extractSubject(token);
//        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
//    }
//
//    private boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date(System.currentTimeMillis()));
//    }
//
//    private Date extractExpiration(String token) {
//        return extractAllClaims(token).getExpiration();
//    }
//
//    public String extractSubject(String token) {
//        return extractAllClaims(token).getSubject();
//    }
//
//    private Claims extractAllClaims(String token) {
//        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
//
//    }
//}
