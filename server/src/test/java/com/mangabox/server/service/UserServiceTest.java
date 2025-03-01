package com.mangabox.server.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.mangabox.server.dto.PatchUserRequest;
import com.mangabox.server.entity.User;
import com.mangabox.server.exception.UserNotFoundException;
import com.mangabox.server.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void testGetUserById_shouldReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("toto");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User result = userService.get(1L);

        assertNotNull(result);
        assertEquals("toto", user.getUsername());
        assertEquals(1L, user.getId());
    }

    @Test
    public void testGetUserById_shouldThrowUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get(1L));
    }

    @Test
    public void testGetUserByUsername_shouldReturnUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("toto");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));

        User result = userService.get("toto");

        assertNotNull(result);
        assertEquals("toto", user.getUsername());
        assertEquals(1L, user.getId());
    }

    @Test
    public void testGetUserByUsername_shouldThrowUserNotFoundException() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.get("toto"));
    }

    @Test
    public void testGetUsers_shouldReturnUsers() {
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername("toto");

        User user2 = new User();
        user2.setId(2L);
        user2.setUsername("tata");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<User> result = userService.get();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals("toto", result.get(0).getUsername());
        assertEquals(2L, result.get(1).getId());
        assertEquals("tata", result.get(1).getUsername());
    }

    @Test
    public void testPatchUser_shouldReturnPatchedUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("toto");

        PatchUserRequest request = new PatchUserRequest();
        request.setUsername("tata");
        request.setPassword("mockedPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User patchedUser = userService.patch(1L, request);

        assertEquals("tata", patchedUser.getUsername());
        assertEquals("encodedPassword", patchedUser.getPassword());

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("mockedPassword");
    }

    @Test
    public void testPatchUser_shouldReturnPatchedUserWithOnlyNewUsername() {
        User user = new User();
        user.setId(1L);
        user.setUsername("toto");
        user.setPassword("mockedPassword");

        PatchUserRequest request = new PatchUserRequest();
        request.setUsername("tata");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User patchedUser = userService.patch(1L, request);

        assertEquals("tata", patchedUser.getUsername());
        assertEquals("mockedPassword", patchedUser.getPassword());

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(0)).encode("mockedPassword");
    }

    @Test
    public void testPatchUser_shouldReturnPatchedUserWithOnlyNewPassword() {
        User user = new User();
        user.setId(1L);
        user.setUsername("toto");
        user.setPassword("mockedPassword");

        PatchUserRequest request = new PatchUserRequest();
        request.setPassword("newPassword");

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        User patchedUser = userService.patch(1L, request);

        assertEquals("toto", patchedUser.getUsername());
        assertEquals("encodedPassword", patchedUser.getPassword());

        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("newPassword");
    }

    @Test
    public void testDeleteUser_shouldDeleteUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("toto");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        userService.delete(1L);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDeleteUser_shouldThrowUserNotFoundException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.delete(1L));
    }

}
