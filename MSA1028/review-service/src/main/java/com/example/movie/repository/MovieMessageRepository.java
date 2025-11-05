package com.example.movie.repository;

import com.example.movie.model.MovieCreatedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieMessageRepository extends JpaRepository<MovieCreatedEvent, Long> {
    boolean existsByEventId(String messageId);
}
