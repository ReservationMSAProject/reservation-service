package com.reservation.reserve.reserve.repository;

import com.reservation.reserve.reserve.domain.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
    List<ConcertEntity> findConcertByDatePass(LocalDateTime date);

}