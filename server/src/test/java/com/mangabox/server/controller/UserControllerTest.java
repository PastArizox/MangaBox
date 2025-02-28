package com.mangabox.server.controller;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserNotFoundException;
import com.mangabox.server.security.service.CustomUserDetailsService;
import com.mangabox.server.security.util.JwtUtils;
import com.mangabox.server.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JwtUtils jwtUtils;

    @MockitoBean
    private CustomUserDetailsService userDetailsService;

    @MockitoBean
    private UserService userService;

    @Test
    public void testGetUser_shouldReturn200AndUserResponse() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setUsername("toto");

        when(userService.get(anyLong())).thenReturn(user);

        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("toto"));
    }

    @Test
    public void testGetUser_shouldReturn404AndUserResponse() throws Exception {
        when(userService.get(anyLong())).thenThrow(new UserNotFoundException("User not found"));

        mockMvc.perform(get("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("User not found"))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    public void testGetUser_shouldReturn400ForInvalidId() throws Exception {
        mockMvc.perform(get("/api/users/abc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUsers_shouldReturn200AndUserResponse() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("toto");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("tata");

        when(userService.get()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].username").value("toto"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].username").value("tata"));
    }

    @Test
    public void testGetUsers_shouldReturn200AndEmptyList() throws Exception {
        when(userService.get()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/users/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

}
