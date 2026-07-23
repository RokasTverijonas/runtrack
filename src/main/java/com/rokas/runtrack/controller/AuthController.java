package com.rokas.runtrack.controller;


import com.rokas.runtrack.dto.AuthResponse;
import com.rokas.runtrack.dto.LoginRequest;
import com.rokas.runtrack.dto.RegisterRequest;
import com.rokas.runtrack.service.AuthService;
import com.rokas.runtrack.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponse register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

}
