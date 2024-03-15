package com.vamae.authorization.service;

import com.vamae.authorization.model.Role;
import com.vamae.authorization.model.User;
import com.vamae.authorization.payload.request.AuthenticationRequest;
import com.vamae.authorization.payload.request.RegisterRequest;
import com.vamae.authorization.payload.response.AuthenticationResponse;
import com.vamae.authorization.repository.UserRepository;
import com.vamae.authorization.security.config.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private static final String USERNAME = "testUsername";
    private static final String PASSWORD = "testPassword";

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authService;

    @Test
    public void testAuthenticateUser_Positive() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        User user = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role(Role.USER)
                .build();

        when(userRepository.findUserByUsername(USERNAME)).thenReturn(Optional.ofNullable(user));
        assert user != null;

        authService.authenticate(authenticationRequest);

        verify(authenticationManager, times(1)).authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        authenticationRequest.getPassword()));
    }

    @Test
    public void testAuthenticateUser_Negative() {
        AuthenticationRequest authenticationRequest = AuthenticationRequest.builder()
                .username(USERNAME)
                .password(PASSWORD + "_wrong")
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid username or password"));

        assertThrows(BadCredentialsException.class, () -> authService.authenticate(authenticationRequest));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, never()).findUserByUsername(anyString());
        verify(jwtService, never()).generateToken(any(User.class));
    }

    @Test
    public void testUserRegister_Positive() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        AuthenticationResponse expectedResponse = AuthenticationResponse.builder()
                .token("sampleAccessToken")
                .build();

        User savedUser = User.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .role(Role.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtService.generateToken(any(User.class))).thenReturn("sampleAccessToken");

        AuthenticationResponse response = authService.register(registerRequest);

        assertEquals(expectedResponse, response);

        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, times(1)).generateToken(any(User.class));
    }


    @Test
    public void testRegister_Negative() {
        RegisterRequest registerRequest = RegisterRequest.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .build();

        when(userRepository.save(any(User.class))).thenThrow(new RuntimeException("Failed to save user"));

        assertThrows(RuntimeException.class, () -> authService.register(registerRequest));

        verify(userRepository, times(1)).save(any(User.class));
        verify(jwtService, never()).generateToken(any(User.class));
    }
}