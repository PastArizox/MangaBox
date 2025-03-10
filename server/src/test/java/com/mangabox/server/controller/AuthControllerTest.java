package com.mangabox.server.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mangabox.server.dto.AuthResponse;
import com.mangabox.server.dto.LoginRequest;
import com.mangabox.server.dto.RegisterRequest;
import com.mangabox.server.exception.UserAlreadyExistsException;
import com.mangabox.server.security.service.CustomUserDetailsService;
import com.mangabox.server.security.util.JwtUtils;
import com.mangabox.server.service.AuthService;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable Spring Security filters
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private ObjectMapper objectMapper; // Pour convertir les objets en JSON

    @Test
    public void testRegister_shouldReturn200AndAuthResponse() throws Exception {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("test");

        AuthResponse authResponse = new AuthResponse("mockedToken");
        when(authService.register(any(RegisterRequest.class))).thenReturn(authResponse);

        // when & then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedToken"));
    }

    @Test
    public void testRegister_shouldReturn409IfUserAlreadyExists() throws Exception {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("test");

        when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new UserAlreadyExistsException("User 'test' already exists"));

        // when & then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("User 'test' already exists"));
    }

    @Test
    public void testRegister_shouldReturn400ForInvalidRequest() throws Exception {
        // given
        RegisterRequest request = new RegisterRequest(); // username and password missing

        // when & then
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testLogin_shouldReturn200AndAuthResponse() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("test");

        AuthResponse authResponse = new AuthResponse("mockedToken");
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedToken"));
    }

    @Test
    public void testLogin_shouldReturn401Unauthorized() throws Exception {
        // given
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("test");
        loginRequest.setPassword("test");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Unable to authenticate with provided credentials."));

        // when & then
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(HttpStatus.UNAUTHORIZED.value()));
    }
}
