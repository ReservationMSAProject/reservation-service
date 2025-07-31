package com.reservation.reserve.event;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationEventProducer {

    private final KafkaTemplate<String, ReservationEventDto> kafkaTemplate;
    private static final String TOPIC = "reservation-events";

    public void sendEvent(ReservationEventDto event) {
        kafkaTemplate.send(TOPIC, event);
    }
}