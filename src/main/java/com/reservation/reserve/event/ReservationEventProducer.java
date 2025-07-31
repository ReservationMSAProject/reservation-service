package com.reservation.reserve.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class ReservationEventProducer {

    private final KafkaTemplate<String, ReservationEventDto> kafkaTemplate;
    private final String topicName;

    public ReservationEventProducer(KafkaTemplate<String, ReservationEventDto> kafkaTemplate,
                                    @Value("${kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendEvent(ReservationEventDto event) {

        try {
            kafkaTemplate.send(topicName, event.reservationId().toString(), event).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaException("Interrupted while sending event to Kafka", e);
        } catch (ExecutionException | TimeoutException e) {
            throw new KafkaException("Failed to send event to Kafka", e);
        }
    }
}