package com.example.demo.repo;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface UserRepo extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE username=?1", nativeQuery = true)
    public User findByUsername(String username);
}
