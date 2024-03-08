package com.example.demo.service.impl;

import com.example.demo.auth.ApplicationUser;
import com.example.demo.entity.User;
import com.example.demo.repo.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class ApplicationUserServiceImpl implements UserDetailsService {

    private final UserRepo userRepo;

    public ApplicationUserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User loadedUser = userRepo.findByUsername(username);
        if (loadedUser == null) {
            throw new UsernameNotFoundException(String.format("Username %s not found !", username));
        }

        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();

        ApplicationUser applicationUser = new ApplicationUser(
                loadedUser.getPassword(),
                loadedUser.getUsername(),
                grantedAuthorities,
                loadedUser.isAccountNonExpired(),
                loadedUser.isAccountNonLocked(),
                loadedUser.isCredentialsNonExpired(),
                loadedUser.isEnabled()
        );

        return applicationUser;
    }
}
