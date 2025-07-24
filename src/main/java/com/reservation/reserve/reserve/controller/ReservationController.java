package com.reservation.reserve.reserve.controller;

import com.reservation.reserve.aop.RequireAuth;
import com.reservation.reserve.filter.UserContext;
import com.reservation.reserve.reserve.dto.ReservationRequestDto;
import com.reservation.reserve.reserve.dto.ReservationResponseDto;
import com.reservation.reserve.reserve.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    @RequireAuth
    public ResponseEntity<ReservationResponseDto> createReservation(@RequestBody ReservationRequestDto requestDto) {
        // 인증 체크 로직 제거됨 - 매우 깔끔!
        String userId = UserContext.getCurrentUser().getUserId();
        ReservationResponseDto response = reservationService.createReservation(requestDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @RequireAuth(roles = {"ADMIN"}) // 다중 역할 지원
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
