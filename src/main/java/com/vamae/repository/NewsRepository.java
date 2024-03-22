package com.vamae.repository;

import com.vamae.entity.News;
import com.vamae.entity.Tag;
import lombok.NonNull;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NewsRepository extends MongoRepository<News, String> {

    Optional<News> findNewsById(String id);

    @NonNull
    List<News> findAll();
}
