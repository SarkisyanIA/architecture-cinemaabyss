package com.cinemaabyss.events.service;

import com.cinemaabyss.events.dto.MovieEvent;
import com.cinemaabyss.events.dto.PaymentEvent;
import com.cinemaabyss.events.dto.UserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventConsumerService {

    @KafkaListener(topics = "${kafka.topics.movie-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMovieEvent(MovieEvent event) {
        log.info("CONSUMED Movie Event: movieId={}, userId={}, action={}, title={}",
                event.getMovieId(), event.getUserId(), event.getAction(), event.getTitle());
    }

    @KafkaListener(topics = "${kafka.topics.user-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUserEvent(UserEvent event) {
        log.info("CONSUMED User Event: userId={}, username={}, action={}, timestamp={}",
                event.getUserId(), event.getUsername(), event.getAction(), event.getTimestamp());
    }

    @KafkaListener(topics = "${kafka.topics.payment-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePaymentEvent(PaymentEvent event) {
        log.info("CONSUMED Payment Event: paymentId={}, userId={}, amount={}, status={}, method={}, timestamp={}",
                event.getPaymentId(), event.getUserId(), event.getAmount(),
                event.getStatus(), event.getMethodType(), event.getTimestamp());
    }
}