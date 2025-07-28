package com.reservation.reserve.reserve.repository;

import com.reservation.reserve.reserve.domain.ConcertEntity;
import com.reservation.reserve.reserve.domain.SeatEntity;
import com.reservation.reserve.reserve.domain.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<SeatEntity, Long> {

    // 콘서트의 예약 가능한 좌석 조회
    @Query("""
                SELECT s FROM SeatEntity s
                WHERE s.venue = :venue AND NOT EXISTS (
                    SELECT 1 FROM ReservationEntity r
                    WHERE r.seat = s
                    AND r.concert = :concert
                    AND r.status IN (com.reservation.reserve.reserve.domain.StatusEnum.CONFIRMED, com.reservation.reserve.reserve.domain.StatusEnum.TEMP_RESERVED)
                )
            """)
    List<SeatEntity> findAvailableSeatsByConcert(@Param("venue") VenueEntity venue, @Param("concert") ConcertEntity concert);
}