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

//        Check username == username?? - playerService.loadByUsername()
//        Check authorities == authorities??
        try {
            Boolean isvalidtoken = jwtTokenUtil.isValidToken(tokenToValidate);
        } catch (JwtException jwte) { //Thrown by the JwtTokenUtil.getAllClaims...() method in validation attempt
//            Better error handling needed here...
            System.out.println("Token cannot be trusted: " + jwte.getMessage());
        }

        Authentication updatedAuthentication = jwtTokenUtil.createNewAuthenticationFromValidToken(tokenToValidate);
        SecurityContextHolder.getContext().setAuthentication(updatedAuthentication);

        httpServletResponse.addHeader(jwtConfig.getAuthorizationHeader(),
                jwtConfig.getBearerPrefix() + jwtTokenUtil.generateNewToken(updatedAuthentication));

//        filterChain.doFilter(httpServletRequest, httpServletResponse);

        /*
        *   Create Mongo Collection of in-use tokens
        *   Ability to delete tokens once expired
        *   Add to collection when new ones created.
         */
    }
}
