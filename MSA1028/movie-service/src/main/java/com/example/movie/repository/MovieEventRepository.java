package com.example.movie.repository;

import com.example.movie.model.MovieCreatedEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieEventRepository extends JpaRepository<MovieCreatedEvent, Long> {
    boolean existsByEventId(String eventId);
}
