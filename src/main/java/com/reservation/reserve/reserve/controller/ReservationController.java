package com.reservation.reserve.reserve.controller;

import com.reservation.reserve.aop.RequireAuth;
import com.reservation.reserve.filter.UserContext;
import com.reservation.reserve.reserve.dto.reservation.ReservationCreateRequest;
import com.reservation.reserve.reserve.dto.reservation.ReservationResponse;
import com.reservation.reserve.reserve.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/create")
    @RequireAuth
    public ResponseEntity<ReservationResponse> createReservation(@RequestBody ReservationCreateRequest requestDto) {

        String email = UserContext.getCurrentUser().getUserId();
        ReservationResponse response = reservationService.createReservation(requestDto, email);
        return ResponseEntity.ok(response);

//        log.info("Create reservation {}", requestDto);
//        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    @RequireAuth(roles = {"ADMIN"})
    public ResponseEntity<Void> cancelReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.ok().build();
    }
}
