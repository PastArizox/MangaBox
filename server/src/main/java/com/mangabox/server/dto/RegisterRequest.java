package com.mangabox.server.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "Username must not be null")
    private String username;

    @NotBlank(message = "Password must not be null")
    private String password;
}
