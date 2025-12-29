package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

     public User getUserFromToken() {
         Authentication auth = SecurityContextHolder.getContext().getAuthentication();
         String username = auth.getName();
         User user = userRepository.findByUsername(username)
                 .orElseThrow(() -> new RuntimeException("User not found"));

         return user;
     }

    public User save(User user) {
        return userRepository.save(user);
    }
}
