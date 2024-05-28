package com.vamae.statistic;

import com.vamae.VamaeApplication;
import com.vamae.statistic.model.Statistic;
import com.vamae.statistic.payload.response.StatisticResponse;
import com.vamae.statistic.service.StatisticService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = VamaeApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StatisticControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatisticService statisticService;

    private Statistic statistic;

    private final String USERNAME = "testAdmin";

    @BeforeEach
    void setUp() {
        statistic = Statistic.builder()
                .username(USERNAME)
                .authCount(1)
                .build();
    }

    @Test
    public void testGetStatistic_userExists() throws Exception {
        StatisticResponse response = new StatisticResponse(statistic.getUsername(), statistic.getAuthCount());
        Mockito.when(statisticService.getStatistic(USERNAME)).thenReturn(response);
        int authCount = response.authCount();
        mockMvc.perform(get("http://localhost:8080/statistic")
                        .param("username", USERNAME)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(USERNAME))
                .andExpect(jsonPath("$.authCount").value(authCount));
    }
}
