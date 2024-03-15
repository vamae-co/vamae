package com.vamae.authorization.controller;

import com.vamae.authorization.payload.request.AuthenticationRequest;
import com.vamae.authorization.payload.request.RegisterRequest;
import com.vamae.authorization.service.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @Test
    public void testRegister() {
        RegisterRequest request = RegisterRequest.builder()
                .username("testUsername")
                .password("testPassword")
                .build();

        authenticationController.register(request);
        verify(authenticationService, times(1)).register(request);
    }

    @Test
    public void testAuthenticate() {
        AuthenticationRequest request = AuthenticationRequest.builder()
                .username("testUsername")
                .password("testPassword")
                .build();

        authenticationController.authenticate(request);
        verify(authenticationService, times(1)).authenticate(request);
    }
}