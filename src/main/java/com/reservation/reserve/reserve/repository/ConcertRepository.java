package com.reservation.reserve.reserve.repository;

import com.reservation.reserve.reserve.domain.ConcertEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConcertRepository extends JpaRepository<ConcertEntity, Long> {


    // 오늘 날짜 이후의 콘서트 목록을 조회하는 쿼리
    @Query("""
            SELECT c FROM ConcertEntity c
            LEFT JOIN FETCH c.venue v
            LEFT JOIN FETCH v.address a
            WHERE c.date >= :date
            ORDER BY c.date, c.name
            """)
    List<ConcertEntity> findConcertByDate(LocalDateTime date);

    // 오늘 날짜 이전의 콘서트 목록을 조회하는 쿼리
    @Query("""
            SELECT c FROM ConcertEntity c
            LEFT JOIN FETCH c.venue v
            LEFT JOIN FETCH v.address a
            WHERE c.date < :date
            ORDER BY c.date, c.name
            """)
    List<ConcertEntity> findPastConcertsByDate(LocalDateTime date);

    // 특정 콘서트 ID로 콘서트를 조회하고, 공연장과 주소를 함께 가져오는 쿼리
    @EntityGraph(attributePaths = {"venue", "venue.seats"})
    Optional<ConcertEntity> findWithDetailsById(@Param("id") Long id);
}