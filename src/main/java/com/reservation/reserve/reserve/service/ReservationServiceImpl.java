package com.reservation.reserve.reserve.service;


import com.reservation.reserve.event.ReservationEventDto;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final ApplicationEventPublisher eventPublisher;


    // 저장
    @Override
    @Transactional
    public ReservationResponse createReservation(ReservationCreateRequest requestDto, String email) {
        ConcertEntity concertEntity = concertRepository.findById(requestDto.getConcertId())
                .orElseThrow(() -> new EntityNotFoundException("Concert not found"));

        SeatEntity seatEntity = seatRepository.findById(requestDto.getSeatId())
                .orElseThrow(() -> new EntityNotFoundException("Seat not found"));

        // 좌석 예약 가능 여부 확인
        boolean isAvailable = !reservationRepository.existsByConcertIdAndSeatIdAndStatusIn(
                requestDto.getConcertId(),
                requestDto.getSeatId(),
                List.of(StatusEnum.TEMP_RESERVED, StatusEnum.CONFIRMED)
        );

        if (!isAvailable) {
            throw new IllegalArgumentException("해당 좌석은 예약 불가능 합니다.");
        }

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

        ReservationEventDto eventDto = new ReservationEventDto(
                savedReservation.getId(),
                savedReservation.getReserverEmail(),
                savedReservation.getConcert().getName(),
                savedReservation.getConcert().getDate(),
                savedReservation.getSeat().getSeatNumber(),
                "CREATE",
                LocalDateTime.now().toString()
        );

        eventPublisher.publishEvent(eventDto);

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
    @Override
    @Transactional
    public void cancelReservation(Long id) {

        ReservationEntity reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));

        reservation.updateStatus(StatusEnum.CANCELLED);
        reservationRepository.save(reservation);

        ReservationEventDto eventDto = new ReservationEventDto(
                reservation.getId(),
                reservation.getReserverEmail(),
                reservation.getConcert().getName(),
                reservation.getConcert().getDate(),
                reservation.getSeat().getSeatNumber(),
                "CANCEL",
                LocalDateTime.now().toString()
        );

        eventPublisher.publishEvent(eventDto);
    }


    // 예약 조회
    @Override
    @Transactional(readOnly = true)
    public ReservationResponse getReservation(Long reservationId) {
        return reservationRepository.findByIdWithDetails(reservationId)
                .map(getReservationEntityReservationResponseFunction())
                .orElseThrow(() -> new EntityNotFoundException("Reservation not found"));
    }


    // 특정 이메일에 대한 모든 예약 조회
    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getAllReservationsByEmail(String email) {
        List<ReservationEntity> reservations = reservationRepository.findAllByReserverEmail(email);

        return reservations.stream()
                .map(getReservationEntityReservationResponseFunction())
                .toList();
    }

    // 특정 콘서트와 좌석에 대한 예약 조회
    @Override
    @Transactional(readOnly = true)
    public List<ReservationResponse> getReservationsByConcertAndSeat(Long concertId, Long seatId) {
        List<ReservationEntity> reservations = reservationRepository.findAllByConcertIdAndSeatId(concertId, seatId);

        return reservations.stream()
                .map(getReservationEntityReservationResponseFunction())
                .toList();
    }

    @Override
    @Transactional
    public ReservationEventDto testKafka() {
        ReservationEventDto eventDto = new ReservationEventDto(
                1L, // 예약 ID
                "test@email.com", // 예약자 이메일
                "테스트 콘서트", // 콘서트명
                LocalDateTime.now().plusDays(1), // 콘서트 날짜
                "A-1", // 좌석 번호
                "CREATE", // 이벤트 타입
                LocalDateTime.now().toString() // 이벤트 발생 시각
        );

        eventPublisher.publishEvent(eventDto);

        return eventDto;
    }

    // 예약 엔티티를 예약 응답으로 변환하는 함수
    // 이 함수는 중복 코드를 줄이기 위해 사용됩니다.
    private static Function<ReservationEntity, ReservationResponse> getReservationEntityReservationResponseFunction() {
        return reservation -> new ReservationResponse(
                reservation.getId(),
                new ReservationResponse.SeatInfo(
                        reservation.getSeat().getId(),
                        reservation.getSeat().getSeatNumber(),
                        reservation.getSeat().getSection(),
                        reservation.getSeat().getGrade(),
                        reservation.getSeat().getPrice()
                ),
                new ReservationResponse.ConcertInfo(
                        reservation.getConcert().getId(),
                        reservation.getConcert().getName(),
                        reservation.getConcert().getDate(),
                        reservation.getConcert().getVenue().getName(),
                        reservation.getConcert().getVenue().getAddress() != null
                                ? reservation.getConcert().getVenue().getAddress().getFullAddress()
                                : ""
                ),
                reservation.getReserverEmail(),
                reservation.getCreateAt(),
                reservation.getExpiresAt(),
                reservation.getStatus()

        );
    }


}
