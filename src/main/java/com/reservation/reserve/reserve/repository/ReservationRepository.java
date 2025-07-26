package com.reservation.reserve.reserve.repository;

import com.reservation.reserve.reserve.domain.ReservationEntity;
import com.reservation.reserve.reserve.domain.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {

    /**
     * 특정 콘서트의 특정 좌석에 대한 활성 예약(TEMP_RESERVED, CONFIRMED) 존재 여부 확인
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.concert.id = :concertId AND r.seat.id = :seatId " +
           "AND r.status IN ('TEMP_RESERVED', 'CONFIRMED')")
    Optional<ReservationEntity> findActiveByConcertAndSeat(@Param("concertId") Long concertId,
                                                          @Param("seatId") Long seatId);

    /**
     * 만료된 임시 예약 조회
     */
    @Query("SELECT r FROM ReservationEntity r WHERE r.status = 'TEMP_RESERVED' AND r.expiresAt < :now")
    List<ReservationEntity> findExpiredTempReservations(@Param("now") LocalDateTime now);

    /**
     * 특정 상태의 예약 개수 조회
     */
    long countByStatus(StatusEnum status);

    /**
     * 특정 콘서트와 좌석에 대한 임시 예약 존재 여부 확인
     */
    boolean existsByConcertIdAndSeatIdAndStatusIn(
            Long concertId,
            Long seatId,
            List<StatusEnum> tempReserved
    );

    /**
     * 특정 이메일에 대한 모든 예약 조회 (대소문자 구분 없음)
     */
    List<ReservationEntity> findAllByReserverEmail(String email);

    /**
     * 특정 콘서트와 좌석에 대한 모든 예약 조회
     */
    List<ReservationEntity> findAllByConcertIdAndSeatId(Long concertId, Long seatId);
}