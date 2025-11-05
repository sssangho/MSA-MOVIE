package com.example.movie.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class MovieCreatedEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;           // 영화 제목
    private String director;        // 감독
    private String category;        // 장르 (예: 액션, 드라마 등)
    private String releaseDate;     // 개봉일
    private String description;     // 간단한 설명

    @Column(unique = true, nullable = false)
    private String eventId = UUID.randomUUID().toString();  // 메시지 고유 식별자

    public MovieCreatedEvent() {}

    public MovieCreatedEvent(String title, String director, String category, String releaseDate, String description) {
        this.title = title;
        this.director = director;
        this.category = category;
        this.releaseDate = releaseDate;
        this.description = description;
    }

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
}
