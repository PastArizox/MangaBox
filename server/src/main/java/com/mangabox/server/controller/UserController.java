package com.mangabox.server.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangabox.server.dto.UserResponse;
import com.mangabox.server.entity.User;
import com.mangabox.server.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private UserService userService;

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        User user = userService.get(id);
        UserResponse response = new UserResponse(user);

        return ResponseEntity.ok(response);
    }

}
