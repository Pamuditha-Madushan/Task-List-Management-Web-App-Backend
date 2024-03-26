package com.example.demo.jwt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsernameAndPasswordAuthenticationRequest {

    private String username;

    private String password;

    private static final Logger logger = LoggerFactory.getLogger(UsernameAndPasswordAuthenticationRequest.class);

    public UsernameAndPasswordAuthenticationRequest() {
    }

    public String getUsername() {
        logger.info("UAPAR - start", username);
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        logger.info("UAPAR -end", password);
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
