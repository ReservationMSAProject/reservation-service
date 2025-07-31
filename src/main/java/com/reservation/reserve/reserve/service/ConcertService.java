package com.reservation.reserve.reserve.service;

import com.reservation.reserve.reserve.dto.concert.ConcertDetailResponse;
import com.reservation.reserve.reserve.dto.concert.ConcertResponse;

import java.util.List;

public interface ConcertService {
    // 오늘 이후의 콘서트 목록 조회
    List<ConcertResponse> getAllConcerts();

    // 특정 콘서트의 빈 좌석 조회
    ConcertDetailResponse getAvailableSeats(Long concertId);
}
