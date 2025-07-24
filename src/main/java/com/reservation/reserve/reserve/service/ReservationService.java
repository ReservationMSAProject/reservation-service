package com.reservation.reserve.reserve.service;

import com.reservation.reserve.reserve.domain.ConcertEntity;
import com.reservation.reserve.reserve.domain.ReservationEntity;
import com.reservation.reserve.reserve.domain.SeatEntity;
import com.reservation.reserve.reserve.domain.StatusEnum;
import com.reservation.reserve.reserve.dto.*;
import com.reservation.reserve.reserve.repository.ConcertRepository;
import com.reservation.reserve.reserve.repository.ReservationRepository;
import com.reservation.reserve.reserve.repository.SeatRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;



    // 저장
    @Transactional
    public ReservationResponseDto createReservation(ReservationRequestDto requestDto) {

        ConcertEntity concertEntity = concertRepository
                .findById(requestDto.concertId())
                .orElseThrow(() -> new EntityNotFoundException("Concert not found"));

        SeatEntity seatEntity = seatRepository
                .findById(requestDto.seatId())
                .orElseThrow(() -> new EntityNotFoundException("Seat not found"));

        ReservationEntity reservation = ReservationEntity.builder()
                .reserverName(requestDto.reserverName())
                .reserverPhone(requestDto.reserverPhone())
                .status(StatusEnum.PROGRESS)
                .build();

        // 연관관계 설정
        reservation.addSeat(seatEntity);
        reservation.addConcert(concertEntity);

        ReservationEntity saved = reservationRepository.save(reservation);
        return new ReservationResponseDto(
                new SeatDto(
                        seatEntity.getSeatNumber(),
                        seatEntity.getSection(),
                        seatEntity.getGrade(),
                        seatEntity.getPrice()
                ),
                new ConcertDto(
                        concertEntity.getName(),
                        concertEntity.getDate(),
                        concertEntity.getLocation()
                ),
                saved.getReserverName(),
                saved.getReserverPhone(),
                saved.getCreateAt(),
                saved.getStatus()
        );
    }

    // 취소
    @Transactional
    public void deleteReservation(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        reservation.updateStatus(StatusEnum.CANCEL);
        reservationRepository.save(reservation);

    }
}

