package com.project.asidesappbe.security;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.project.asidesappbe.security.PlayerRole.GROUPADMIN;
import static com.project.asidesappbe.security.PlayerRole.GROUPPLAYER;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final PlayerService playerService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, PlayerService playerService) {
        this.passwordEncoder = passwordEncoder;
        this.playerService = playerService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/players/getall")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/players/register")
                .permitAll()
                .antMatchers(HttpMethod.POST, "/api/v1/players/login")
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
