package com.example.demo.security;

import com.example.demo.jwt.JwtConfig;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.jwt.JwtTokenVerifier;
import com.example.demo.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import com.example.demo.service.impl.ApplicationUserServiceImpl;
import com.example.demo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.crypto.SecretKey;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    private final ApplicationUserServiceImpl applicationUserService;
    private final UserService userService;

    private final JwtConfig jwtConfig;

    private final SecretKey secretKey;


    @Autowired
    public ApplicationSecurityConfig(
            PasswordEncoder passwordEncoder,
            ApplicationUserServiceImpl applicationUserService,
            UserService userService,
            JwtConfig jwtConfig,
            SecretKey secretKey) {
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.userService = userService;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    private static final Logger logger = LoggerFactory.getLogger(ApplicationSecurityConfig.class);

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        logger.error("attempt security - start");

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
                .addFilterBefore
                        (new JwtUsernameAndPasswordAuthenticationFilter
                                        (authenticationManager(), jwtConfig, secretKey),
                                UsernamePasswordAuthenticationFilter.class
                        )
                .addFilterBefore(new JwtTokenVerifier(jwtConfig, secretKey),
                        JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(
                        "/api/v1/user/register/**",
                        "/api/v1/user/login/**" /*,
                        "/api/v1/tasks/business/create",
                        "/api/v1/tasks/business/list",
                        "/api/v1/tasks/business/find-solo-task/**",
                        "/api/v1/tasks/business/modify/**",
                        "/api/v1/tasks/business/delete/**"
                        */

                )
                .permitAll()
                .anyRequest()
                .authenticated();

        logger.error("attempt security - end");

    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        logger.error("attempt security authentication - start");
        auth.userDetailsService(applicationUserService).passwordEncoder(passwordEncoder);
        logger.error("attempt security authentication - end");
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


   /* @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        logger.error("attempt security provider - start");
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        logger.error("attempt security provider - end");
        return provider;
    }

    */

    @Bean
    public GrantedAuthorityDefaults grantedAuthorityDefaults() {
        return new GrantedAuthorityDefaults("");
    }


}
