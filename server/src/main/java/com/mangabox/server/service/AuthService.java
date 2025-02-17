package com.mangabox.server.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mangabox.server.dto.AuthResponse;
import com.mangabox.server.dto.LoginRequest;
import com.mangabox.server.dto.RegisterRequest;
import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserAlreadyExistsException;
import com.mangabox.server.repository.UserRepository;
import com.mangabox.server.security.CustomUserDetails;
import com.mangabox.server.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UserAlreadyExistsException(String.format(
                    "User '%s' already exists", request.getUsername()));
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        String token = jwUtils.generateToken(user.getUsername());

        return new AuthResponse(token);
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String token = jwUtils.generateToken(userDetails.getUsername());

        return new AuthResponse(token);
    }

}
