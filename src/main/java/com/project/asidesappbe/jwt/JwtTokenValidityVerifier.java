package com.project.asidesappbe.jwt;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenValidityVerifier extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

//        get header from request using jwtconfig->header
//        Check it's !null && starts with "Bearer "
//        Trim the header to obtain the token
//        Parse claims
//        Parse username from claims
//        Parse authorities from claims
//        Get user details
//        Check username == username?? - playerService.loadByUsername()
//        Check authorities == authorities??
//        Set Authentication
//        Set Security Context Holder

//        Generate new token with updated exp date
//        Set token to response header

//        Create Mongo Collection of in-use tokens
//        Ability to delete tokens once expired
//        Add to collection when new ones created.

    }
}
