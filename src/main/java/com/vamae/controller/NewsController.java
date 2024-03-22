package com.vamae.controller;

import com.vamae.entity.News;
import com.vamae.entity.Tag;
import com.vamae.payload.request.CreateNewsRequest;
import com.vamae.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/news")
public class NewsController {

    private final NewsService newsService;

    @GetMapping
    public ResponseEntity<List<News>> getAllNews() {
        return ResponseEntity.ok(newsService.getAllNews());
    }

    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(
            @PathVariable String id
    ) {
        return ResponseEntity.ok(newsService.getNewsById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<News> createNews(
            @RequestBody CreateNewsRequest request
    ) {
        return ResponseEntity.ok(newsService.createNews(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity.HeadersBuilder<?> deleteNewsById(
            @PathVariable String id
    ) {
        newsService.deleteNewsById(id);
        return ResponseEntity.noContent();
    }
}
