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


    //콘서트의 빈 좌석 조회
    @Query("""
                SELECT s FROM SeatEntity s
                LEFT JOIN ReservationEntity r ON r.seat = s AND r.concert = :concert AND r.status = com.reservation.reserve.reserve.domain.StatusEnum.CONFIRMED
                WHERE s.venue = :venue
                AND r.id IS NULL
            """)
    List<SeatEntity> findAvailableSeatsByConcert(@Param("venue") VenueEntity venue, @Param("concert") ConcertEntity concert);


}