package com.mangabox.server.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mangabox.server.dto.AuthResponse;
import com.mangabox.server.dto.RegisterRequest;
import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserAlreadyExistsException;
import com.mangabox.server.repository.UserRepository;
import com.mangabox.server.security.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwUtils;

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

}
