package com.example.demo.service.impl;


import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.request.RequestUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.SQLIntegrityConstraintViolationException;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.jwt.JwtConfig;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.process.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserServiceImpl applicationUserService;

    private final JwtConfig jwtConfig;
   private final JwtTokenUtil jwtTokenUtil;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, ApplicationUserServiceImpl applicationUserService, JwtConfig jwtConfig, JwtTokenUtil jwtTokenUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.jwtConfig = jwtConfig;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public CommonResponseDTO registerUser(RequestUserDTO requestUserDTO) throws IOException {
        User existingUser = userRepo.findByUsername(requestUserDTO.getUsername());
        if (existingUser != null) {
            throw new SQLIntegrityConstraintViolationException("Username " +requestUserDTO.getUsername() + " exists already !");
        }

        User newUser = new User();
        newUser.setUsername(requestUserDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(requestUserDTO.getPassword()));

        User savedUser = userRepo.save(newUser);

        return new CommonResponseDTO(201, "User " +requestUserDTO.getUsername()+ " registered successfully ...", savedUser, null);
    }

    @Override
    public CommonResponseDTO loginUser(LoginUserDTO loginUserDTO, HttpServletResponse response) throws IOException {
        User loginUser = userRepo.findByUsername(loginUserDTO.getUsername());
        if (loginUser == null) {
            throw new UsernameNotFoundException("Username " +loginUserDTO.getUsername() + " not found !");
        }
        if (!passwordEncoder.matches(loginUserDTO.getPassword(), loginUser.getPassword())) {
            throw new UnAuthorizedException("Invalid Username or Password !");
        }

        String accessToken = jwtTokenUtil.generateToken(loginUser.getUsername());

        response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + " " + accessToken);



        return new CommonResponseDTO(200,
                "User " +loginUserDTO.getUsername()+ " logged in successfully ...",
                accessToken,
                null);
    }
}
