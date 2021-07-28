package com.project.asidesappbe.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;

public class JwtTokenValidityVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final JwtTokenUtil jwtTokenUtil;

    public JwtTokenValidityVerifier(JwtConfig jwtConfig, JwtTokenUtil jwtTokenUtil) {
        this.jwtConfig = jwtConfig;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = httpServletRequest.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(authorizationHeader) || !authorizationHeader.startsWith(jwtConfig.getBearerPrefix())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String tokenToValidate = authorizationHeader.replace(jwtConfig.getBearerPrefix(), "");

        try {
            if(jwtTokenUtil.isValidToken(tokenToValidate)) {
                Authentication updatedAuthentication = jwtTokenUtil.createNewAuthenticationFromValidToken(tokenToValidate);
                SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

                httpServletResponse.addHeader(jwtConfig.getAuthorizationHeader(),
                        jwtConfig.getBearerPrefix() + jwtTokenUtil.generateNewToken(updatedAuthentication));
            }

        } catch (JwtException jwte) { //Thrown by the JwtTokenUtil.getAllClaims...() method in validation attempt
//            Better error handling needed here...
            System.out.println("Token cannot be trusted: " + jwte.getMessage());
            httpServletResponse.setHeader("error", jwte.getMessage());
            httpServletResponse.setStatus(SC_FORBIDDEN);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
