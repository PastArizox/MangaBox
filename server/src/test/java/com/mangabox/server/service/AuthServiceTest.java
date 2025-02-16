package com.mangabox.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mangabox.server.dto.AuthResponse;
import com.mangabox.server.dto.RegisterRequest;
import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserAlreadyExistsException;
import com.mangabox.server.repository.UserRepository;
import com.mangabox.server.security.JwtUtils;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegiter_shouldAddUserToDatabase() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("test");

        // when
        authService.register(request);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegister_shouldThrowUserAlreadyExistsException() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("test");

        when(userRepository.existsByUsername("test")).thenReturn(true);

        // when
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    public void testRegister_shouldEncodePassword() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("test");

        // when
        authService.register(request);

        // then
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void testRegister_shouldGenerateJwtToken() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("test");
        request.setPassword("test");

        when(jwtUtils.generateToken(anyString())).thenReturn("mockedToken");

        // when
        AuthResponse response = authService.register(request);

        // then
        assertNotNull(response.getToken());
        assertEquals("mockedToken", response.getToken());
        verify(jwtUtils).generateToken("test");
    }

}
