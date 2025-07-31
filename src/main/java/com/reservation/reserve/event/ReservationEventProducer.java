package com.reservation.reserve.event;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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
        kafkaTemplate.send(topicName, event);
    }
}