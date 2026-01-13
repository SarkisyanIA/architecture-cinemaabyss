package com.cinemaabyss.events.service;

import com.cinemaabyss.events.dto.MovieEvent;
import com.cinemaabyss.events.dto.PaymentEvent;
import com.cinemaabyss.events.dto.UserEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventProducerService {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topics.movie-events}")
    private String movieEventsTopic;

    @Value("${kafka.topics.user-events}")
    private String userEventsTopic;

    @Value("${kafka.topics.payment-events}")
    private String paymentEventsTopic;

    public void sendMovieEvent(MovieEvent event) {
        log.info("Sending movie event: {}", event);
        kafkaTemplate.send(movieEventsTopic, event.getMovieId(), event);
    }

    public void sendUserEvent(UserEvent event) {
        log.info("Sending user event: {}", event);
        kafkaTemplate.send(userEventsTopic, event.getUserId(), event);
    }

    public void sendPaymentEvent(PaymentEvent event) {
        log.info("Sending payment event: {}", event);
        kafkaTemplate.send(paymentEventsTopic, event.getPaymentId(), event);
    }
}
