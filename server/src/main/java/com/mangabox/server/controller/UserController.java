package com.mangabox.server.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mangabox.server.dto.PatchUserRequest;
import com.mangabox.server.dto.UserResponse;
import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UnauthorizedActionException;
import com.mangabox.server.security.model.CustomUserDetails;
import com.mangabox.server.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<UserResponse>> get() {
        List<User> users = userService.get();

        List<UserResponse> userResponses = users
                .stream()
                .map(UserResponse::new)
                .toList();

        return ResponseEntity.ok(userResponses);
    }

    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponse> get(@PathVariable Long id) {
        User user = userService.get(id);
        UserResponse response = new UserResponse(user);

        return ResponseEntity.ok(response);
    }

    @PatchMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<UserResponse> patch(
            @PathVariable Long id,
            @Valid @RequestBody PatchUserRequest request,
            @AuthenticationPrincipal CustomUserDetails authenticatedUser) {

        if (!authenticatedUser.getId().equals(id))
            throw new UnauthorizedActionException("You are not allowed to modify this user.");

        User user = userService.patch(id, request);

        UserResponse response = new UserResponse(user);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails authenticatedUser) {

        if (!authenticatedUser.getId().equals(id))
            throw new UnauthorizedActionException("You are not allowed to delete this user.");

        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

}
