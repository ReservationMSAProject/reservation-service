package com.reservation.reserve.event;

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
    public void handleReservationEvent(ReservationEventDto response) {
        try {

            reservationEventProducer.sendEvent(response);

        } catch (Exception e) {
            log.error("카프카 이벤트 생성 에러 발생: {}", response.reservationId(), e);
            throw e;
        }
    }


    @Recover
    public void recover(Exception e, ReservationEventDto response) {
        log.error("예약 ID [{}]의 카프카 이벤트 발행이 3회 재시도 후 최종 실패했습니다. 오류: {}",
                response.reservationId(), e.getMessage(), e);
        handleFailedEvent(response, e);
    }

    private void handleFailedEvent(ReservationEventDto response, Exception e) {
        try {
            log.info("예약 ID [{}]의 실패 이벤트를 데이터베이스에 기록합니다", response.reservationId());

            log.warn("예약 ID [{}]의 실패 알림을 관리자에게 전송합니다", response.reservationId());

        } catch (Exception failureHandlingException) {
            log.error("예약 ID [{}]의 실패 이벤트 후처리 중 추가 오류 발생: {}",
                    response.reservationId(), failureHandlingException.getMessage(), failureHandlingException);
        }

        log.error("예약 ID [{}]를 데드 레터 큐로 이동시킵니다. 수동 처리가 필요합니다", response.reservationId());
    }

}
