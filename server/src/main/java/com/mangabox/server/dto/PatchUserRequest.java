package com.mangabox.server.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatchUserRequest {

    private String username;
    private String password;

}
