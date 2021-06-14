package com.project.asidesappbe.security;

import com.project.asidesappbe.jwt.JwtConfig;
import com.project.asidesappbe.jwt.JwtTokenUtil;
import com.project.asidesappbe.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.project.asidesappbe.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.project.asidesappbe.security.PlayerRole.GROUPADMIN;
import static com.project.asidesappbe.security.PlayerRole.GROUPPLAYER;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final PlayerService playerService;
    private final JwtConfig jwtConfig;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, PlayerService playerService,
                                     JwtConfig jwtConfig, JwtTokenUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.playerService = playerService;
        this.jwtConfig = jwtConfig;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /*
    Ok so getting a forbidden on login now.
    And token is not yet being added as a response header on signup.
    Could have something to do with auth pattern.
    Need to make sure token is generated at login and reg...
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig, jwtTokenUtil))
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/players/register", "/api/v1/players/login")
                .permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/players/getall")
                .hasAnyRole(GROUPPLAYER.name(), GROUPADMIN.name())
//                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(playerService);

        return authenticationProvider;
    }
}
