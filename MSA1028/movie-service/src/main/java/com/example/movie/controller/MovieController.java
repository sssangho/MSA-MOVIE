package com.example.movie.controller;

import com.example.movie.model.Movie;
import com.example.movie.model.MovieCreatedEvent;
import com.example.movie.repository.MovieEventRepository;
import com.example.movie.repository.MovieRepository;
import com.example.movie.service.MovieEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieRepository movieRepository;
    private final MovieEventProducer eventProducer;
    private final MovieEventRepository movieEventRepository;

    /**
     * 🎬 모든 영화 조회
     */
    @GetMapping
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    /**
     * 🎬 ID로 영화 조회
     */
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🎬 영화 등록 + RabbitMQ 이벤트 발행
     */
    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        // 1️⃣ 영화 DB 저장
        Movie savedMovie = movieRepository.save(movie);

        // 2️⃣ MQ 이벤트 객체 생성
        MovieCreatedEvent event = new MovieCreatedEvent(
                savedMovie.getTitle(),
                savedMovie.getDirector(),
                savedMovie.getCategory(),
                (savedMovie.getReleaseDate() != null ? savedMovie.getReleaseDate().toString() : "미정"),
                "새로운 영화가 등록되었습니다!"
        );

        // 3️⃣ MQ 발행
        eventProducer.sendMovieCreatedEvent(event);

        // 4️⃣ 이벤트 로그 DB 저장
        if (!movieEventRepository.existsByEventId(event.getEventId())) {
            movieEventRepository.save(event);
            System.out.println("💾 [DB] 이벤트 로그 저장 완료 → " + event.getTitle());
        } else {
            System.out.println("⚠️ 이미 처리된 이벤트 ID → " + event.getEventId());
        }

        System.out.println("🎬 [MovieController] 영화 등록 및 MQ 발행 완료 → " + savedMovie.getTitle());
        return savedMovie;
    }


    /**
     * 🎬 영화 수정
     */
    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @RequestBody Movie movie) {
        return movieRepository.findById(id)
                .map(existingMovie -> {
                    movie.setId(id);
                    return ResponseEntity.ok(movieRepository.save(movie));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🎬 영화 삭제
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable Long id) {
        return movieRepository.findById(id)
                .map(movie -> {
                    movieRepository.delete(movie);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
