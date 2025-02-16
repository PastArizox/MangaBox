package com.mangabox.server.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("Unable to authenticate with provided credentials.");
            }

            return new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        } catch (UsernameNotFoundException exception) {
            throw new BadCredentialsException("Unable to authenticate with provided credentials.");
        }
    }

}
