package com.example.demo.service.impl;


import com.example.demo.dto.UserDTO;
import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.request.RequestUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.UnAuthorizedException;
import com.example.demo.jwt.JwtConfig;
import com.example.demo.jwt.JwtTokenUtil;
import com.example.demo.repo.UserRepo;
import com.example.demo.security.ApplicationSecurityConfig;
import com.example.demo.service.UserService;
import com.example.demo.util.mapper.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserServiceImpl applicationUserService;

   // private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;
   private final JwtTokenUtil jwtTokenUtil;

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, ApplicationUserServiceImpl applicationUserService, UserMapper userMapper, JwtConfig jwtConfig, JwtTokenUtil jwtTokenUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;
        this.userMapper = userMapper;
        this.jwtConfig = jwtConfig;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    public CommonResponseDTO registerUser(RequestUserDTO requestUserDTO) throws IOException {
        User existingUser = userRepo.findByUsername(requestUserDTO.getUsername());
        if (existingUser == null) {

            UserDTO userDTO = new UserDTO(
                    requestUserDTO.getUsername(),
                    passwordEncoder.encode(requestUserDTO.getPassword()),
                    true,
                    true,
                    true,
                    true


            );



            userRepo.save(userMapper.toUser(userDTO));

            return new CommonResponseDTO(201,  requestUserDTO.getUsername() + " registered successfully ...", requestUserDTO.getUsername(), new ArrayList<>());

        } else {
                return new CommonResponseDTO(409, requestUserDTO.getUsername() + " exists already !", "ALREADY_EXISTS", new ArrayList<>());
        }

    }

    @Override
    public CommonResponseDTO loginUser(LoginUserDTO loginUserDTO, HttpServletResponse response) throws IOException {
        logger.debug("service impl login user - start");
        User loginUser = userRepo.findByUsername(loginUserDTO.getUsername());
        if (loginUser == null || !passwordEncoder.matches(loginUserDTO.getPassword(), loginUser.getPassword())) {
            throw new UnAuthorizedException("Invalid Username or Password !");
        }

        /*
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDTO.getUsername(), loginUserDTO.getPassword())
        );

        if (authentication != null) {
            return new CommonResponseDTO(200,
                    "User " +loginUserDTO.getUsername()+ " logged in successfully ...",
                    null,
                    // accessToken,
                    null);
        }

        else {
            throw new BadCredentialsException("Authentication failed");
        }

         */


       // String accessToken = jwtTokenUtil.generateToken(loginUser.getUsername());

       // response.addHeader(jwtConfig.getAuthorizationHeader(), jwtConfig.getTokenPrefix() + " " + accessToken);

        logger.debug("service impl login user - end");

        return new CommonResponseDTO(200,
                "User " +loginUserDTO.getUsername()+ " logged in successfully ...",
                response.getHeader("Authorization"),
                // accessToken,
                null);


    }
}
