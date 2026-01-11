package com.cinemaabyss.events.conig;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${kafka.topics.movie-events}")
    private String movieEventsTopic;

    @Value("${kafka.topics.user-events}")
    private String userEventsTopic;

    @Value("${kafka.topics.payment-events}")
    private String paymentEventsTopic;

    // 1. Producer Configuration
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                "org.springframework.kafka.support.serializer.JsonSerializer");
        props.put(ProducerConfig.ACKS_CONFIG, "1");
        props.put(ProducerConfig.RETRIES_CONFIG, 3);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 5);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);

        // Для десериализации JSON
        props.put("spring.json.type.mapping",
                "movieEvent:com.cinemaabyss.events.dto.MovieEvent," +
                        "userEvent:com.cinemaabyss.events.dto.UserEvent," +
                        "paymentEvent:com.cinemaabyss.events.dto.PaymentEvent");

        return props;
    }

    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        KafkaTemplate<String, Object> template = new KafkaTemplate<>(producerFactory());
        template.setObservationEnabled(true);  // Для метрик
        return template;
    }

    // 2. Consumer Configuration
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "events-group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, 1000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.springframework.kafka.support.serializer.JsonDeserializer");

        // Критически важно для десериализации!
        props.put("spring.json.trusted.packages", "com.cinemaabyss.events.dto");
        props.put("spring.json.type.mapping",
                "movieEvent:com.cinemaabyss.events.dto.MovieEvent," +
                        "userEvent:com.cinemaabyss.events.dto.UserEvent," +
                        "paymentEvent:com.cinemaabyss.events.dto.PaymentEvent");
        props.put("spring.json.remove.type.headers", false);
        props.put("spring.json.use.type.headers", true);

        return props;
    }

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object>
    kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, Object> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.setCommonErrorHandler(errorHandler());

        // Настройки контейнера
        factory.getContainerProperties().setPollTimeout(3000);

        return factory;
    }

    @Bean
    public CommonErrorHandler errorHandler() {
        return new DefaultErrorHandler(
                (record, exception) -> {
                    log.error("Ошибка обработки сообщения: topic={}, partition={}, offset={}, exception={}",
                            record.topic(), record.partition(), record.offset(), exception.getMessage(), exception);
                },
                new FixedBackOff(1000L, 3)
        );
    }

    // 3. Topic Configuration
    @Bean
    public NewTopic movieEventsTopic() {
        log.info("Создание топика: {}", movieEventsTopic);
        return TopicBuilder.name(movieEventsTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic userEventsTopic() {
        log.info("Создание топика: {}", userEventsTopic);
        return TopicBuilder.name(userEventsTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic paymentEventsTopic() {
        log.info("Создание топика: {}", paymentEventsTopic);
        return TopicBuilder.name(paymentEventsTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        KafkaAdmin admin = new KafkaAdmin(configs);
        admin.setFatalIfBrokerNotAvailable(true);
        admin.setAutoCreate(true);
        return admin;
    }
}