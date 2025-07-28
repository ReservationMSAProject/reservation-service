package com.reservation.reserve.reserve.controller;

import com.reservation.reserve.reserve.dto.ApiResponse;
import com.reservation.reserve.reserve.dto.concert.ConcertDetailResponse;
import com.reservation.reserve.reserve.dto.concert.ConcertResponse;
import com.reservation.reserve.reserve.service.ConcertService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/reserve/api/concerts")
public class ConcertController {

    private final ConcertService concertService;

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ConcertResponse>>> getAllConcerts() {

        List<ConcertResponse> concerts = concertService.getAllConcerts();
        return ResponseEntity.ok(ApiResponse.success(concerts));
    }

    @GetMapping("/detail/{concertId}")
    public ResponseEntity<ApiResponse<ConcertDetailResponse>> getAvailableSeats(@PathVariable("concertId") Long concertId) {

        ConcertDetailResponse availableSeats = concertService.getAvailableSeats(concertId);
        return ResponseEntity.ok(ApiResponse.success(availableSeats));
    }
}
