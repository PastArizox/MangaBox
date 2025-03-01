package com.mangabox.server.dto;

import com.mangabox.server.entity.User;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UserResponse {

    private User user;

    public Long getId() {
        return user.getId();
    }

    public String getUsername() {
        return user.getUsername();
    }

}
