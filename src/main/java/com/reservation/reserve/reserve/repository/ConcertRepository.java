package com.reservation.reserve.reserve.repository;

import com.reservation.reserve.reserve.domain.ConcertEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConcertRepository extends JpaRepository<ConcertEntity, Long> {
}