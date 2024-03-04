package com.example.demo.service.impl;


import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.request.RequestUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.InternalServerErrorException;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.process.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepo userRepo ,PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CommonResponseDTO registerUser(RequestUserDTO requestUserDTO) throws IOException {
        User existingUser = userRepo.findByUsername(requestUserDTO.getUsername());
        if (existingUser != null) {
            throw new InternalServerErrorException("Username exists already !");
        }

        User newUser = new User();
        newUser.setUsername(requestUserDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));

        User savedUser = userRepo.save(newUser);

        return new CommonResponseDTO(200, "User registered successfully ...", savedUser, null);
    }

    @Override
    public CommonResponseDTO loginUser(LoginUserDTO loginUserDTO) throws IOException {

        return new CommonResponseDTO(200, "User registered successfully ...", null, null);
    }
}
