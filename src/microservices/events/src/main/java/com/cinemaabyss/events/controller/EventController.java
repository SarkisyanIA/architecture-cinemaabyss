package com.cinemaabyss.events.controller;

import com.cinemaabyss.events.dto.MovieEvent;
import com.cinemaabyss.events.dto.PaymentEvent;
import com.cinemaabyss.events.dto.UserEvent;
import com.cinemaabyss.events.service.EventConsumerService;
import com.cinemaabyss.events.service.EventProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventProducerService eventProducerService;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", true);
        response.put("service", "events-service");
        response.put("timestamp", Instant.now().toString());
        response.put("message", "Events service is running");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/movie")
    public ResponseEntity<MovieEvent> createMovieEvent(@Valid @RequestBody MovieEvent movieEvent) {
        log.info("Received movie event request: {}", movieEvent);
        try {
            eventProducerService.sendMovieEvent(movieEvent);
            movieEvent.setStatus("success");
            return ResponseEntity.status(HttpStatus.CREATED).body(movieEvent);
        } catch (Exception e) {
            movieEvent.setStatus("error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(movieEvent);
        }
    }

    @PostMapping("/user")
    public ResponseEntity<UserEvent> createUserEvent(@Valid @RequestBody UserEvent userEvent) {
        log.info("Received user event request: {}", userEvent);
        try {
            eventProducerService.sendUserEvent(userEvent);
            userEvent.setStatus("success");
            return ResponseEntity.status(HttpStatus.CREATED).body(userEvent);
        } catch (Exception e) {
            userEvent.setStatus("error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userEvent);
        }
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentEvent> createPaymentEvent(@Valid @RequestBody PaymentEvent paymentEvent) {
        log.info("Received payment event request: {}", paymentEvent);
        try {
            eventProducerService.sendPaymentEvent(paymentEvent);
            paymentEvent.setStatus("success");
            return ResponseEntity.status(HttpStatus.CREATED).body(paymentEvent);
        } catch (Exception e) {
            paymentEvent.setStatus("error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(paymentEvent);
        }
    }
}
