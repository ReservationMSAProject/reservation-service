package com.reservation.reserve.reserve.service;


import com.reservation.reserve.reserve.domain.ConcertEntity;
import com.reservation.reserve.reserve.domain.ReservationEntity;
import com.reservation.reserve.reserve.domain.SeatEntity;
import com.reservation.reserve.reserve.domain.StatusEnum;
import com.reservation.reserve.reserve.dto.reservation.ReservationCreateRequest;
import com.reservation.reserve.reserve.dto.reservation.ReservationResponse;
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
// 저장
    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest requestDto, String email) {

        ConcertEntity concertEntity = concertRepository.findById(requestDto.getConcertId())
                .orElseThrow(() -> new EntityNotFoundException("Concert not found"));

        SeatEntity seatEntity = seatRepository
                .findById(requestDto.getSeatId())
                .orElseThrow(() -> new EntityNotFoundException("Seat not found"));

        ReservationEntity reservation = ReservationEntity.builder()
                .reserverEmail(email)
                .status(StatusEnum.TEMP_RESERVED)
                .build();

        // 연관관계 설정
        reservation.addSeat(seatEntity);
        reservation.addConcert(concertEntity);

        // 예약 저장
        ReservationEntity savedReservation = reservationRepository.save(reservation);

        // 주소 정보가 있는 경우 전체 주소 문자열 생성
        String addressInfo = savedReservation.getConcert().getVenue().getAddress() != null
                ? savedReservation.getConcert().getVenue().getAddress().getFullAddress()
                : "";

        return new ReservationResponse(
                savedReservation.getId(),
                new ReservationResponse.SeatInfo(
                        savedReservation.getSeat().getId(),
                        savedReservation.getSeat().getSeatNumber(),
                        savedReservation.getSeat().getSection(),
                        savedReservation.getSeat().getGrade(),
                        savedReservation.getSeat().getPrice()
                ),
                new ReservationResponse.ConcertInfo(
                        savedReservation.getConcert().getId(),
                        savedReservation.getConcert().getName(),
                        savedReservation.getConcert().getDate(),
                        savedReservation.getConcert().getVenue().getName(),
                        addressInfo
                ),
                savedReservation.getReserverEmail(),
                savedReservation.getCreateAt(),
                savedReservation.getExpiresAt(),
                savedReservation.getStatus()
        );
    }


   // 취소
    @Transactional
    public void cancelReservation(Long id) {
        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
        reservation.updateStatus(StatusEnum.CANCELLED);
        reservationRepository.save(reservation);
    }

/*
    // 공연 목록 조회
    @Transactional(readOnly = true)
    public List<ConcertDto> getConcerts() {
        List<ConcertEntity> concertEntities = concertRepository.findAll();
        return concertEntities.stream()
                .map(concert -> new ConcertDto(
                        concert.getName(),
                        concert.getDate(),
                        concert.getLocation()
                ))
                .toList();
    }

    // 공연 상세 조회
    @Transactional(readOnly = true)
    public ConcertDto getConcertDetails(Long concertId) {
        ConcertEntity concertEntity = concertRepository
                .findById(concertId)
                .orElseThrow(() -> new EntityNotFoundException("Concert not found"));

        return new ConcertDto(
                concertEntity.getName(),
                concertEntity.getDate(),
                concertEntity.getLocation()
        );
    }

    @Transactional(readOnly = true)
    public List<SeatDto> getSeats() {}*/

}

