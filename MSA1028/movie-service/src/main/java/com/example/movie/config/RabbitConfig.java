package com.example.movie.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;

@Configuration
@EnableRabbit
public class RabbitConfig {

    // 🎬 영화 등록 이벤트용 큐 이름
    public static final String MOVIE_QUEUE = "movie-queue";

    /**
     * durable = true → 서버 재시작 후에도 큐 유지
     */
    @Bean
    public Queue movieQueue() {
        return new Queue(MOVIE_QUEUE, true);
    }
}
