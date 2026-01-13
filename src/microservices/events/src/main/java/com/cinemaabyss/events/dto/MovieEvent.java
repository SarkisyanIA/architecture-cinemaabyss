package com.cinemaabyss.events.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieEvent {
    @JsonProperty("movie_id")
    private String movieId;
    private String title;
    private String action;
    @JsonProperty("user_id")
    private String userId;
    private String status;
}

