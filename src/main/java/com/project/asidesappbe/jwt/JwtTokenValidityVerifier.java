package com.project.asidesappbe.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenValidityVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;

    public JwtTokenValidityVerifier(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

//        get header from request using jwtconfig->header
        String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());
//        Check it's !null && starts with "Bearer "
        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getBearerPrefix())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
//        Trim the header to obtain the token
        String tokenToValidate = authorizationHeader.replace(jwtConfig.getBearerPrefix(), "");

//        Parse claims
//        Parse username from claims
//        Parse authorities from claims
//        Get user details
//        Check username == username?? - playerService.loadByUsername()
//        Check authorities == authorities??
        try {
//            Check if token is valid here
        } catch (JwtException jwte) { //Thrown by the JwtToken.getAllClaims... method
//            Handle untrusted token here, Iain
        }
//        Set Authentication
//        Set Security Context Holder

//        Generate new token with updated exp date
//        Set token to response header

//        Create Mongo Collection of in-use tokens
//        Ability to delete tokens once expired
//        Add to collection when new ones created.

    }
}
