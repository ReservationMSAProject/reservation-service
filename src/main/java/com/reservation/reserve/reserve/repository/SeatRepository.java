package com.reservation.reserve.reserve.repository;

import com.reservation.reserve.reserve.domain.ConcertEntity;
import com.reservation.reserve.reserve.domain.SeatEntity;
import com.reservation.reserve.reserve.domain.StatusEnum;
import com.reservation.reserve.reserve.domain.VenueEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    // 콘서트의 예약 가능한 좌석 조회
    @Query("""
                SELECT s FROM SeatEntity s
                WHERE s.venue = :venue AND NOT EXISTS (
                    SELECT 1 FROM ReservationEntity r
                    WHERE r.seat = s
                    AND r.concert = :concert
                    AND r.status IN :statuses
                )
            """)
    List<SeatEntity> findAvailableSeatsByConcert(@Param("venue") VenueEntity venue, @Param("concert") ConcertEntity concert, @Param("statuses") List<StatusEnum> statuses);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM SeatEntity s WHERE s.id = :seatId")
    Optional<SeatEntity> findByIdForUpdate(@Param("seatId") Long seatId);
}