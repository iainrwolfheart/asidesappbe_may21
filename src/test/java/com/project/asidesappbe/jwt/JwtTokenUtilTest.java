package com.project.asidesappbe.jwt;

import com.project.asidesappbe.models.Player;
import com.project.asidesappbe.services.PlayerService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
/*
How to make private methods testable in isolation?
 */
    @Test
    @DisplayName("GetAllClaims should return all claims from token")
    void testGetAllClaimsFromToken() {
        Jws<Claims> jws = jwtTokenUtil.getAllClaimsFromToken(testToken);
        System.out.println(jws.toString());
        assertEquals(jws.getBody().getSubject(), "testPlayer");
    }

    @Test
    @DisplayName("extractSpecifiedClaim should return claim specified as parameter")
    void testExtractSpecifiedClaimReturnsRequiredClaim() {
        assertEquals("testPlayer", jwtTokenUtil.getUsernameFromToken(testToken));
    }

    @Test
    @DisplayName("Test a valid token passes expiry check")
    void testValidTokenPassesExpirationChecks() {
        when(playerService.userExists("testPlayer")).thenReturn(true);
        assertTrue(jwtTokenUtil.isValidToken(testToken));
    }

}