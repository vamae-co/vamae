package com.vamae.authorization.controller;

import com.vamae.VamaeApplication;
import com.vamae.entity.News;
import com.vamae.entity.Tag;
import com.vamae.payload.request.CreateNewsRequest;
import com.vamae.service.NewsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = VamaeApplication.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NewsControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NewsService newsService;

    private News news;

    private String jwtToken;

    @BeforeEach
    void setUp() {
        news = News.builder()
                .id("1")
                .date(new Date())
                .header("Test Header")
                .content("Test Content")
                .imageUrl("http://example.com/image.jpg")
                .tags(Arrays.asList(Tag.NEWS, Tag.UPDATE))
                .build();

        jwtToken = System.getenv("TEST_JWT_TOKEN");
    }

    @Test
    void getAllNews() throws Exception {
        List<News> newsList = Arrays.asList(news);

        when(newsService.getAllNews()).thenReturn(newsList);

        mockMvc.perform(get("/news"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(news.getId()))
                .andExpect(jsonPath("$[0].header").value(news.getHeader()))
                .andExpect(jsonPath("$[0].content").value(news.getContent()));
    }

    @Test
    void getNewsById() throws Exception {
        when(newsService.getNewsById("1")).thenReturn(news);

        mockMvc.perform(get("/news/1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(news.getId()))
                .andExpect(jsonPath("$.header").value(news.getHeader()))
                .andExpect(jsonPath("$.content").value(news.getContent()));
    }

    @Test
    void createNews() throws Exception {
        CreateNewsRequest request = new CreateNewsRequest(news.getHeader(), news.getContent(), news.getImageUrl(), news.getTags());
        when(newsService.createNews(any(CreateNewsRequest.class))).thenReturn(news);

        mockMvc.perform(post("/news/create")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"header\":\"Test Header\",\"content\":\"Test Content\",\"imageUrl\":\"http://example.com/image.jpg\",\"tags\":[\"NEWS\",\"UPDATE\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(news.getId()))
                .andExpect(jsonPath("$.header").value(news.getHeader()))
                .andExpect(jsonPath("$.content").value(news.getContent()));
    }

    @Test
    void deleteNewsById() throws Exception {
        doNothing().when(newsService).deleteNewsById("1");

        mockMvc.perform(delete("/news/1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());
    }
}
