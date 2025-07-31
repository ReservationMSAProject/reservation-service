package com.reservation.reserve.reserve.service;

import com.reservation.reserve.event.ReservationEventDto;
import com.reservation.reserve.reserve.dto.reservation.ReservationCreateRequest;
import com.reservation.reserve.reserve.dto.reservation.ReservationResponse;

import java.util.List;

public interface ReservationService {

    // 예약 생성
    ReservationResponse createReservation(ReservationCreateRequest requestDto, String email);

    // 예약 취소
    void cancelReservation(Long reservationId);

    // 예약 조회
    ReservationResponse getReservation(Long reservationId);

    // 사용자의 모든 예약 조회
    List<ReservationResponse> getAllReservationsByEmail(String email);

    // 특정 콘서트와 좌석에 대한 예약 조회
    List<ReservationResponse> getReservationsByConcertAndSeat(Long concertId, Long seatId);

    // 카프카 테스트
    ReservationEventDto testKafka();
}
