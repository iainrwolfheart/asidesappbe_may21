package com.project.asidesappbe.jwt;

import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.services.PlayerService;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;

import static com.project.asidesappbe.security.PlayerRole.GROUPADMIN;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestPropertySource(locations="classpath:test.properties")
class JwtTokenUtilTest {

    private static JwtTokenUtil jwtTokenUtil;
    private static Player testPlayer;
    private static Authentication testAuthentication;
    private String testToken;
    private static PlayerService playerService;
    private JwtConfig jwtConfig;

//    Create a mock token
//    Test claims parsed from token

    /*
    Need to clean this up. Override JwtConfig properties?
     */
    @BeforeEach
    void initTestVars() {
        testPlayer = new Player("testPlayer", "testPlayer@email.com", "testPassword");
        testPlayer.setAuthorities(GROUPADMIN.getGrantedAuthorities());
        testAuthentication = new UsernamePasswordAuthenticationToken(
                testPlayer.getUsername(),
                testPlayer.getPassword(),
                new HashSet<>());
        playerService = mock(PlayerService.class);
        this.jwtConfig = new JwtConfig();
        this.jwtConfig.setSecretKey("securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecuresecureafmate");
        this.jwtConfig.setBearerPrefix("Bearer ");
        this.jwtConfig.setTokenValidityTimeInDays(2);
        jwtTokenUtil = new JwtTokenUtil(playerService, jwtConfig);
        testToken = jwtTokenUtil.generateNewToken(testAuthentication);
    }

    @Test
    @DisplayName("Test a valid token passes expiry check")
    void testValidTokenPassesExpirationChecks() {
        when(playerService.userExists("testPlayer")).thenReturn(true);
        assertTrue(jwtTokenUtil.isValidToken(testToken));
    }

    @Test
    @DisplayName("An expired token should throw a JwtException.")
    void testExpiredTokenThrowsException() {
        when(playerService.userExists("testPlayer")).thenReturn(true);
        this.jwtConfig.setTokenValidityTimeInDays(0);
        String failToken = jwtTokenUtil.generateNewToken(testAuthentication);
        try {
            Boolean isValidToken = jwtTokenUtil.isValidToken(failToken);
        fail();
        } catch(JwtException jwte) {
            System.out.println("Test failed expectedly, well done. " + jwte.getMessage());
        }
    }

    @Test
    @DisplayName("A token should fail validity checks when the Claim username doesn't exist in DB.")
    void testValidityCheckReturnsFalseWhenUsernameNotFound() {
        when(playerService.userExists("testPlayer")).thenReturn(false);
        assertFalse(jwtTokenUtil.isValidToken(testToken));
    }

}