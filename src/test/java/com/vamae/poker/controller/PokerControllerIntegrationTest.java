package com.vamae.poker.controller;

import com.vamae.VamaeApplication;
import com.vamae.poker.lib.controllers.Poker;
import com.vamae.poker.lib.models.dto.TableDto;
import com.vamae.poker.lib.models.records.Settings;
import com.vamae.poker.model.PokerGameSession;
import com.vamae.poker.model.responses.CreateResponse;
import com.vamae.poker.service.PokerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.security.Principal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VamaeApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class PokerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PokerService pokerService;

    private PokerGameSession pokerGameSession;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        TableDto tableDto = Poker.create(new Settings(25, 1000));

        pokerGameSession = PokerGameSession.builder()
                .id("1")
                .playersLinks(new HashMap<>())
                .table(tableDto)
                .build();

        jwtToken = System.getenv("TEST_JWT_TOKEN");
    }

    @Test
    void getAllNotStartedGames() throws Exception {
        List<PokerGameSession> newsList = Collections.singletonList(pokerGameSession);

        when(pokerService.findAllNotStarted()).thenReturn(newsList);

        mockMvc.perform(get("/poker/games")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(pokerGameSession.getId()))
                .andExpect(jsonPath("$[0].countOfPlayers").value(pokerGameSession.getTable().players().size()));
    }

    @Test
    void create() throws Exception {
        when(pokerService.create(any(Settings.class), any(Principal.class))).thenReturn(new CreateResponse(pokerGameSession.getId()));

        mockMvc.perform(post("/poker/games")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"smallBlind\":25,\"startingBank\":1000}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pokerGameSession.getId()));
    }
}
