package com.vamae.service;

import com.vamae.entity.News;
import com.vamae.payload.request.CreateNewsRequest;
import com.vamae.repository.NewsRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    public News getNewsById(String id){
        return newsRepository.findNewsById(id)
                .orElseThrow(() -> new NoSuchElementException("News not found!"));
    }

    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    public void deleteNewsById(String id) {
        newsRepository.deleteById(id);
    }

    public News createNews(@NotNull CreateNewsRequest request) {
        return News.builder()
                .date(request.date())
                .content(request.content())
                .imageUrl(request.imageUrl())
                .tags(request.tags())
                .build();
    }
}
