package com.cinemaabyss.events.controller;

import com.cinemaabyss.events.dto.MovieEvent;
import com.cinemaabyss.events.dto.PaymentEvent;
import com.cinemaabyss.events.dto.UserEvent;
import com.cinemaabyss.events.service.EventConsumerService;
import com.cinemaabyss.events.service.EventProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventController {

    private final EventProducerService eventProducerService;

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Events service is running");
    }

    @PostMapping("/movie")
    public ResponseEntity<MovieEvent> createMovieEvent(@Valid @RequestBody MovieEvent movieEvent) {
        log.info("Received movie event request: {}", movieEvent);
        eventProducerService.sendMovieEvent(movieEvent);
        return ResponseEntity.ok(movieEvent);
    }

    @PostMapping("/user")
    public ResponseEntity<UserEvent> createUserEvent(@Valid @RequestBody UserEvent userEvent) {
        log.info("Received user event request: {}", userEvent);
        eventProducerService.sendUserEvent(userEvent);
        return ResponseEntity.ok(userEvent);
    }

    @PostMapping("/payment")
    public ResponseEntity<PaymentEvent> createPaymentEvent(@Valid @RequestBody PaymentEvent paymentEvent) {
        log.info("Received payment event request: {}", paymentEvent);
        eventProducerService.sendPaymentEvent(paymentEvent);
        return ResponseEntity.ok(paymentEvent);
    }
}
