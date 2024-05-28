package com.vamae.statistic;

import com.vamae.authorization.model.Role;
import com.vamae.authorization.model.User;
import com.vamae.authorization.repository.UserRepository;
import com.vamae.authorization.security.config.JwtService;
import com.vamae.statistic.model.Statistic;
import com.vamae.statistic.payload.response.StatisticResponse;
import com.vamae.statistic.repository.StatisticRepository;
import com.vamae.statistic.service.StatisticService;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser(username = "testuser")
public class StatisticControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticRepository statisticRepository;

    @MockBean
    private StatisticService statisticService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationManager authenticationManager;

    private final String username = "testuser";
    private final String password = "password";
    private final int authCount = 1;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .username(username)
                .password(password)
                .isActive(true)
                .role(Role.USER)
                .balance(1000)
                .build();

        Statistic statistic = Statistic.builder()
                .id("1")
                .username(username)
                .authCount(authCount)
                .build();

        when(statisticRepository.findByUsername(anyString())).thenReturn(Optional.of(statistic));
        when(statisticService.getStatistic(username)).thenReturn(new StatisticResponse(username, authCount));
        when(userRepository.findUserByUsername(username)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn(password);
        when(jwtService.generateToken(user)).thenReturn("test-token");
        when(jwtService.extractClaim(anyString(), any())).thenReturn(null);
        when(authenticationManager.authenticate(any())).thenReturn(new UsernamePasswordAuthenticationToken(username, password));
    }

    @Test
    public void testGetStatistic() throws Exception {
        String testToken = "test_token";
        mockMvc.perform(get("http://localhost:8080/statistic")
                        .param("username", username)
                        .header("Authorization", "Bearer " + testToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.authCount").value(authCount));
    }

    @Test
    public void testAuthenticate() throws Exception {
        mockMvc.perform(post("http://localhost:8080/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));

        verify(statisticService, times(1)).updateAuthCount(username);
    }
}
