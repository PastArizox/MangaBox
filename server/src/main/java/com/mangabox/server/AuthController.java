package com.mangabox.server;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @PostMapping(value = "/register", produces = { "application/json" })
    public Map<String, String> register() {

        // TODO: register a user

        Map<String, String> response = new HashMap<>();
        response.put("message", "register");
        return response;
    }

}
