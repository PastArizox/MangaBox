package com.mangabox.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mangabox.server.dto.AuthResponse;
import com.mangabox.server.dto.LoginRequest;
import com.mangabox.server.dto.RegisterRequest;
import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserAlreadyExistsException;
import com.mangabox.server.repository.UserRepository;
import com.mangabox.server.security.CustomAuthenticationManager;
import com.mangabox.server.security.CustomUserDetails;
import com.mangabox.server.security.JwtUtils;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private CustomAuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    public void testRegiter_shouldAddUserToDatabase() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("toto");
        request.setPassword("tata");

        // when
        authService.register(request);

        // then
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegister_shouldThrowUserAlreadyExistsException() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("toto");
        request.setPassword("tata");

        when(userRepository.existsByUsername("toto")).thenReturn(true);

        // when & then
        assertThrows(UserAlreadyExistsException.class, () -> authService.register(request));
    }

    @Test
    public void testRegister_shouldEncodePassword() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("toto");
        request.setPassword("tata");

        // when
        authService.register(request);

        // then
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void testRegister_shouldGenerateJwtToken() {
        // given
        RegisterRequest request = new RegisterRequest();
        request.setUsername("toto");
        request.setPassword("tata");

        when(jwtUtils.generateToken(anyString())).thenReturn("mockedToken");

        // when
        AuthResponse response = authService.register(request);

        // then
        assertNotNull(response.getToken());
        assertEquals("mockedToken", response.getToken());
        verify(jwtUtils).generateToken("toto");
    }

    @Test
    public void testLogin_shouldGenerateJwtToken() {
        // given
        LoginRequest request = new LoginRequest();
        request.setUsername("toto");
        request.setPassword("tata");

        User user = new User();
        user.setUsername("toto");
        user.setPassword("encodedPassword");

        CustomUserDetails userDetails = new CustomUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                Collections.emptyList());

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.generateToken(anyString())).thenReturn("mockedToken");

        // when
        AuthResponse response = authService.login(request);

        // then
        assertNotNull(response.getToken());
        assertEquals("mockedToken", response.getToken());
        verify(jwtUtils).generateToken("toto");
    }

    @Test
    public void testLogin_shouldThrowBadCredentialsException() {
        // given
        LoginRequest request = new LoginRequest();
        request.setUsername("toto");
        request.setPassword("tata");

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Unable to authenticate with provided credentials."));

        // when & then
        assertThrows(BadCredentialsException.class, () -> authService.login(request));
    }

}
