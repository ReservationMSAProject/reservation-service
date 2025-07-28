package com.reservation.reserve.reserve.service;// src/main/java/com/reservation/reserve/reserve/service/ConcertService.java

import com.reservation.reserve.reserve.domain.ConcertEntity;
import com.reservation.reserve.reserve.domain.SeatEntity;
import com.reservation.reserve.reserve.dto.concert.ConcertDetailResponse;
import com.reservation.reserve.reserve.dto.concert.ConcertResponse;
import com.reservation.reserve.reserve.repository.ConcertRepository;
import com.reservation.reserve.reserve.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;

    // 오늘 이후의 콘서트 목록 조회
    @Transactional(readOnly = true)
    public List<ConcertResponse> getAllConcerts() {

        return concertRepository.findConcertByDate(LocalDateTime.now()).stream()
                .map(concert -> new ConcertResponse(
                        concert.getId(),
                        concert.getName(),
                        concert.getDate(),
                        new ConcertResponse.VenueInfo(
                                concert.getVenue().getId(),
                                concert.getVenue().getName(),
                                concert.getVenue().getAddress().getFullAddress()
                        )
                ))
                .toList();
    }

    // 특정 콘서트의 빈 좌석
    @Transactional(readOnly = true)
    public ConcertDetailResponse getAvailableSeats(Long concertId) {

        ConcertEntity concert = concertRepository.findById(concertId)
                .orElseThrow(() -> new IllegalArgumentException("콘서트를 찾을 수 없습니다."));

        List<SeatEntity> availableSeatsByConcert = seatRepository.findAvailableSeatsByConcert(concert.getVenue(), concert);

        return new ConcertDetailResponse(
                concert.getId(),
                concert.getName(),
                concert.getDate(),
                new ConcertDetailResponse.VenueInfo(
                        concert.getVenue().getId(),
                        concert.getVenue().getName(),
                        concert.getVenue().getAddress().getFullAddress()
                ),
                availableSeatsByConcert.stream()
                        .map(seat -> new ConcertDetailResponse.SeatInfo(
                                seat.getId(),
                                seat.getSeatNumber(),
                                seat.getSection(),
                                seat.getGrade(),
                                seat.isActive(),
                                seat.getPrice()
                        ))
                        .toList()
        );
    }
}