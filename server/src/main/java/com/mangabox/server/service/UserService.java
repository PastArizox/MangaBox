package com.mangabox.server.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserNotFoundException;
import com.mangabox.server.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

}
