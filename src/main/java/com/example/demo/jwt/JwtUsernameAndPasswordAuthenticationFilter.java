package com.example.demo.jwt;


import com.example.demo.auth.ApplicationUser;
import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;



public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private final JwtConfig jwtConfig;

    private final SecretKey secretKey;


    private static final Logger logger = LoggerFactory.getLogger(JwtUsernameAndPasswordAuthenticationFilter.class);

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager, JwtConfig jwtConfig, SecretKey secretKey) {
        this.authenticationManager = authenticationManager;
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;

        setFilterProcessesUrl("/api/v1/user/authenticate");

        logger.info("jwtUsernameAndPasswordAuthenticationFilter constructor - start", authenticationManager);
        logger.debug("",jwtConfig);
        logger.info("", secretKey);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        logger.info("attempt authentication - start");

        try {
            UsernameAndPasswordAuthenticationRequest authenticationRequest =
                    new ObjectMapper().readValue(request.getInputStream(),
                            UsernameAndPasswordAuthenticationRequest.class);

            logger.info("Deserialized authentication request: {}", authenticationRequest);
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authenticationRequest.getUsername(),
                    authenticationRequest.getPassword(),
                    new ArrayList<>()
            );

            Authentication authenticate = authenticationManager.authenticate(authentication);

            logger.debug("attempt authentication - end" + authenticate);
            return authenticate;
        } catch (IOException e) {
            logger.trace("Error while attempting authentication: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (AuthenticationException ex) {
            logger.debug("Authentication failed: " + ex.getMessage());
            throw ex;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


        logger.info("==>> SUCCESSFUL Authentication!  " + authResult.toString());
       // chain.doFilter(request, response);

        try {
            String token = Jwts.builder()
                    .setSubject(authResult.getName())
                    .setIssuedAt(new Date())
                    .setExpiration(java.sql.Date.valueOf(LocalDate.now()
                            .plusDays(jwtConfig.getTokenExpirationAfterDays())))
                    .signWith(secretKey)
                    .compact();

         /*
            String username = (String) authResult.getPrincipal();
            String token = jwtTokenUtil.generateToken(username);


          */

            response.addHeader(jwtConfig.getAuthorizationHeader(),
                    jwtConfig.getTokenPrefix() + " " + token);


            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{ \"token\": \"" + jwtConfig.getTokenPrefix() + token + "\" }");


            logger.info("Token generated and response setup successful.");


        } catch (Exception e) {
            logger.error("Error during successful authentication: " + e.getMessage());
            throw new ServletException(e);
        }


            /*
        } catch (JwtException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Error generating JWT token: " + e.getMessage());
        }

         */

/*
        logger.info("successful Authentication - end");

 */
    }
}
