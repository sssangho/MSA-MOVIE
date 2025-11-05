package com.example.movie.service;

import com.example.movie.config.RabbitConfig;
import com.example.movie.model.MovieCreatedEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class MovieEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private final Gson gson = new Gson();

    public MovieEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * 🎬 영화 등록 이벤트를 RabbitMQ로 전송
     */
    public void sendMovieCreatedEvent(MovieCreatedEvent event) {
        String json = gson.toJson(event);
        rabbitTemplate.convertAndSend(RabbitConfig.MOVIE_QUEUE, json); // ✅ 변경된 큐 이름 사용
        System.out.println("🎬 [MovieEventProducer] 영화 등록 이벤트 전송 완료 → " + event.getTitle());
    }
}
