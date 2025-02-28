package com.mangabox.server.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mangabox.server.dto.PatchUserRequest;
import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserNotFoundException;
import com.mangabox.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> get() {
        List<User> users = userRepository
                .findAll();

        return users;
    }

    public User get(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user;
    }

    public User get(String username) {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return user;
    }

    public User patch(Long id, PatchUserRequest request) {
        User user = this.get(id);

        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);

        return user;
    }

}
