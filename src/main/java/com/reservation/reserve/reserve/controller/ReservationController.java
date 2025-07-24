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

        String email = UserContext.getCurrentUser().getUserId();
        ReservationResponseDto response = reservationService.createReservation(requestDto, email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @RequireAuth(roles = {"ADMIN"})
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
        return ResponseEntity.ok().build();
    }
}
