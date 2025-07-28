package com.reservation.reserve.reserve.service;

import com.reservation.reserve.reserve.domain.ConcertEntity;
import com.reservation.reserve.reserve.domain.SeatEntity;
import com.reservation.reserve.reserve.dto.concert.ConcertDetailResponse;
import com.reservation.reserve.reserve.dto.concert.ConcertResponse;
import com.reservation.reserve.reserve.repository.ConcertRepository;
import com.reservation.reserve.reserve.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        ConcertEntity concert = concertRepository.findWithDetailsById(concertId)
                .orElseThrow(() -> new EntityNotFoundException("콘서트를 찾을 수 없습니다."));

        // 해당 콘서트에서 예약 가능한 좌석 목록
        List<SeatEntity> availableSeats = seatRepository.findAvailableSeatsByConcert(concert.getVenue(), concert);

        // 해당 공연장의 모든 좌석 조회
        List<SeatEntity> allSeats = concert.getVenue().getSeats();
        Set<SeatEntity> availableSeatSet = new HashSet<>(availableSeats);

        return new ConcertDetailResponse(
                concert.getId(),
                concert.getName(),
                concert.getDate(),
                new ConcertDetailResponse.VenueInfo(
                        concert.getVenue().getId(),
                        concert.getVenue().getName(),
                        concert.getVenue().getAddress().getFullAddress()
                ),
                allSeats.stream()
                        .map(seat -> new ConcertDetailResponse.SeatInfo(
                                seat.getId(),
                                seat.getSeatNumber(),
                                seat.getSection(),
                                seat.getGrade(),
                                availableSeatSet.contains(seat),
                                seat.getPrice()
                        ))
                        .toList()
        );
    }
}