package com.example.movie.service;

import com.example.movie.config.RabbitConfig;
import com.example.movie.model.MovieCreatedEvent;
import com.example.movie.repository.MovieMessageRepository;
import com.google.gson.Gson;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MovieEventConsumer {

    private MovieMessageRepository repository;
    private final Gson gson = new Gson();

    public MovieEventConsumer(MovieMessageRepository repository) {
        this.repository = repository;
    }

    /**
     * 🎬 movie-service에서 전송한 영화 등록 이벤트를 수신
     */
    @RabbitListener(queues = RabbitConfig.MOVIE_QUEUE)
    public void receiveMovieEvent(String jsonMessage) {
        // JSON → 객체 변환
        MovieCreatedEvent event = gson.fromJson(jsonMessage, MovieCreatedEvent.class);

        // ✅ messageId 중복 방지
        if (repository.existsByEventId(event.getEventId())) {
            System.out.println("⚠️ Duplicate message skipped: " + event.getEventId());
            return; // 이미 저장된 메시지면 무시
        }

        repository.save(event);
        System.out.println("✅ Saved message: " + event.getTitle());
    }

}
