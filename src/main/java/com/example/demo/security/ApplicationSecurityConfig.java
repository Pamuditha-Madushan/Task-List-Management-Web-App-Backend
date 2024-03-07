package com.example.demo.security;

import com.example.demo.jwt.JwtConfig;
import com.example.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.demo.service.process.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    private final JwtConfig jwtConfig;

    private final SecretKey secretKey;

    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, UserService userService, JwtConfig jwtConfig, SecretKey secretKey) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    @Autowired


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedHeaders(List.of("Authorization",
                "Cache-Control", "Content-Type"));
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT",
                "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        corsConfiguration.setAllowCredentials(false);
        corsConfiguration.setExposedHeaders(List.of("Authorization"));

        http
                .csrf().disable()
                .cors().configurationSource(request -> corsConfiguration)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter
                        (new JwtUsernameAndPasswordAuthenticationFilter
                                (authenticationManager(), jwtConfig, secretKey)
                        )
                .authorizeRequests()
                .antMatchers(
                        "/api/v1/user/register/**",
                        "/api/v1/user/login/**"
                )
                .permitAll()
                .anyRequest()
                .authenticated();

    }
}
