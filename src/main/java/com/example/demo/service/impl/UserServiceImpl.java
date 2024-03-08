package com.example.demo.service.impl;


import com.example.demo.dto.request.LoginUserDTO;
import com.example.demo.dto.request.RequestUserDTO;
import com.example.demo.dto.response.core.CommonResponseDTO;
import com.example.demo.entity.User;
import com.example.demo.exception.SQLIntegrityConstraintViolationException;
import com.example.demo.exception.UnAuthorizedException;
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

import java.io.IOException;

@Service
@Transactional
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserServiceImpl applicationUserService;

    //private final AuthenticationManager authenticationManager;

    public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder, ApplicationUserServiceImpl applicationUserService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.applicationUserService = applicationUserService;

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

        return new CommonResponseDTO(200, "User " +requestUserDTO.getUsername()+ " registered successfully ...", savedUser, null);
    }

    @Override
    public CommonResponseDTO loginUser(LoginUserDTO loginUserDTO) throws IOException {
        User loginUser = userRepo.findByUsername(loginUserDTO.getUsername());
        if (loginUser == null) {
            throw new UsernameNotFoundException("Username " +loginUserDTO.getUsername() + " not found !");
        }
        if (!passwordEncoder.matches(loginUserDTO.getPassword(), loginUser.getPassword())) {
            throw new UnAuthorizedException("Invalid Username or Password !");
        }



        // User user = (User) userDetails;
        // String token = UsernamePasswordAuthenticationFilter.

        return new CommonResponseDTO(200, "User " +loginUserDTO.getUsername()+ " logged in successfully ...", loginUser, null);
    }
}
