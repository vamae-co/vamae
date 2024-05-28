package com.vamae.authorization.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vamae.VamaeApplication;
import com.vamae.connect4.entity.Connect4Game;
import com.vamae.connect4.lib.controller.Connect4;
import com.vamae.connect4.lib.controller.GameBoardController;
import com.vamae.connect4.lib.entity.GameBoard;
import com.vamae.connect4.lib.enums.Piece;
import com.vamae.connect4.model.dto.GameDto;
import com.vamae.connect4.payload.request.InitializationRequest;
import com.vamae.connect4.service.Connect4GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VamaeApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class Connect4ControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private Connect4GameService connect4GameService;
    private Principal principal;
    private Connect4Game connect4Game;
    private String jwtToken;
    private final String FIRST_PLAYER = "firstPlayer";
    private final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeEach
    public void setUp() {
        principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn(FIRST_PLAYER);

        connect4Game = Connect4Game.builder()
                .id("1")
                .firstPlayerUsername(FIRST_PLAYER)
                .secondPlayerUsername("secondPlayer")
                .betSum(100)
                .game(new Connect4(new GameBoardController(new GameBoard(new ArrayList<>(10), 10))))
                .currentPlayer(Piece.PLAYER_1)
                .isWinFlag(false)
                .build();

        jwtToken = System.getenv("TEST_JWT_TOKEN");
    }

    @Test
    @WithMockUser
    public void testGetAllGames() throws Exception {
        GameDto gameDto = new GameDto("1", FIRST_PLAYER, 50);
        List<GameDto> gameDtoList = Collections.singletonList(gameDto);
        Mockito.when(connect4GameService.getAllGames()).thenReturn(gameDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/connect4/games")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].gameId").value(connect4Game.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstPlayerUsername").value(connect4Game.getFirstPlayerUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].bet").value(connect4Game.getBetSum() / 2));
    }

    @Test
    @WithMockUser
    public void testGetGameById() throws Exception {
        Mockito.when(connect4GameService.getGameById("1")).thenReturn(connect4Game);

        mockMvc.perform(MockMvcRequestBuilders.get("/connect4/game/1")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(connect4Game.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstPlayerUsername").value(connect4Game.getFirstPlayerUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.secondPlayerUsername").value(connect4Game.getSecondPlayerUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.betSum").value(connect4Game.getBetSum()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.currentPlayer").value(connect4Game.getCurrentPlayer().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.winFlag").value(connect4Game.isWinFlag()));
    }

    @Test
    @WithMockUser
    public void testInitGame() throws Exception {
        InitializationRequest request = new InitializationRequest(10, 10, 50);
        String requestBody = objectMapper.writeValueAsString(request);
        Mockito.when(connect4GameService.init(Mockito.any(), Mockito.any(Principal.class))).thenReturn("1");

        mockMvc.perform(post("/connect4/game/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
                        .principal(principal)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(connect4Game.getId()));
    }
}
