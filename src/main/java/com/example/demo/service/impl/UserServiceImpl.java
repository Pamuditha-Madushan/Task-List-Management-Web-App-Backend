package com.example.demo.service.impl;


import com.example.demo.dto.request.RequestUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.exception.InternalServerErrorException;
import com.example.demo.service.process.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final Map<String, String> userCredentials = new HashMap<>();

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CommonResponseDTO registerUser(RequestUserDTO requestUserDTO) throws IOException {
        if (userCredentials.containsKey(requestUserDTO.getUsername())) {
           throw new InternalServerErrorException("Username exists already !");
        }

        String hashedPassword = passwordEncoder.encode(requestUserDTO.getPassword());
        userCredentials.put(requestUserDTO.getUsername(), hashedPassword);

        return new CommonResponseDTO(200, "User registered successfully ...", userCredentials, null);
    }
}
