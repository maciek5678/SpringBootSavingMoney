package com.example.demo.service;

import com.example.demo.dto.JwtResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;


public interface AuthService {
    JwtResponse authenticateUser(LoginRequest loginRequest);
    String registerUser(RegisterRequest signUpRequest);
}