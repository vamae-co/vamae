package com.vamae.authorization.service;

import com.vamae.authorization.model.Role;
import com.vamae.authorization.model.User;
import com.vamae.authorization.payload.request.AuthenticationRequest;
import com.vamae.authorization.payload.request.RegisterRequest;
import com.vamae.authorization.payload.response.AuthenticationResponse;
import com.vamae.authorization.repository.UserRepository;
import com.vamae.authorization.security.config.JwtService;
import com.vamae.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .isActive(true)
                .role(Role.USER)
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        User user = userRepository.findUserByUsername(request.username())
                .orElseThrow(
                        () -> new UserNotFoundException(
                                String.format("User with username %s not found", request.username())
                        )
                );

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
