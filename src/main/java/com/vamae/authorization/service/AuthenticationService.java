package com.vamae.authorization.service;

import com.vamae.authorization.model.Role;
import com.vamae.authorization.model.User;
import com.vamae.authorization.payload.request.AuthenticationRequest;
import com.vamae.authorization.payload.request.RegisterRequest;
import com.vamae.authorization.payload.response.AuthenticationResponse;
import com.vamae.authorization.repository.UserRepository;
import com.vamae.authorization.security.config.JwtService;
import com.vamae.statistic.service.StatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final StatisticService statisticService;

    public AuthenticationResponse register(RegisterRequest request) {
        User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .isActive(true)
                .role(Role.USER)
                .balance(1000)
                .build();
        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user = userRepository.findUserByUsername(request.username())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        statisticService.getAuthTimes(request.username());

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
