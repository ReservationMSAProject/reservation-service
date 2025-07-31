package com.reservation.reserve.event;

import com.reservation.reserve.reserve.dto.reservation.ReservationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventHandler {

    private final ReservationEventProducer reservationEventProducer;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            retryFor = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void handleReservationCreated(ReservationEventDto response) {
        try {

            reservationEventProducer.sendEvent(response);

        } catch (Exception e) {
            log.error("카프카 이벤트 생성 에러 발생: {}", response.reservationId(), e);
            throw e;
        }
    }


    @Recover
    public void recover(Exception e, ReservationResponse response) {
        log.error("All retry attempts failed for reservation event: {}", response.id(), e);
        handleFailedEvent(response, e);
    }

    private void handleFailedEvent(ReservationResponse response, Exception e) {
        try {

            log.info("Saving failed event log for reservation: {}", response.id());

            log.warn("Sending failure notification for reservation: {}", response.id());

        } catch (Exception failureHandlingException) {
            log.error("Failed to handle failed event: {}", response.id(), failureHandlingException);
        }

        log.error("Moving to dead letter handling: {}", response.id());
    }
}
