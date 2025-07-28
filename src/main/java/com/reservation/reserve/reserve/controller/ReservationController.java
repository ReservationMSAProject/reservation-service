package com.reservation.reserve.reserve.controller;

import com.reservation.reserve.aop.RequireAuth;
import com.reservation.reserve.filter.UserContext;
import com.reservation.reserve.reserve.dto.ApiResponse;
import com.reservation.reserve.reserve.dto.reservation.ReservationCreateRequest;
import com.reservation.reserve.reserve.dto.reservation.ReservationResponse;
import com.reservation.reserve.reserve.service.ReservationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reserve/api/reservations")
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    // 예약 생성
    @PostMapping("/create")
    @RequireAuth
    public ResponseEntity<ApiResponse<ReservationResponse>> createReservation(@RequestBody ReservationCreateRequest requestDto) {

        String email = UserContext.getCurrentUser().getUserId();
        ReservationResponse response = reservationService.createReservation(requestDto, email);
        return ResponseEntity.ok(ApiResponse.success(response));

    }

    // 예약 취소
    @PutMapping("/cancel/{reservationId}")
    @RequireAuth(roles = {"USER", "ADMIN"})
    public ResponseEntity<ApiResponse<Void>>  cancelReservation(@PathVariable("reservationId") Long reservationId) {

        reservationService.cancelReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.ofSuccess());

    }

    // 예약 단건 조회 (예약 ID로)
    @GetMapping("/check/{reservationId}")
    @RequireAuth(roles = {"USER"})
    public ResponseEntity<ApiResponse<ReservationResponse>> getReservationById(@PathVariable("reservationId") Long reservationId) {

        ReservationResponse response = reservationService.getReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 특정 이메일(사용자)의 모든 예약 조회
    @GetMapping("/mine")
    @RequireAuth(roles = {"USER", "ADMIN"})
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservationsByUserEmail() {

        String email = UserContext.getCurrentUser().getUserId();
        List<ReservationResponse> responses = reservationService.getAllReservationsByEmail(email);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }

    // 특정 콘서트와 좌석에 대한 모든 예약 목록 조회
    @GetMapping("/concert/{concertId}/seat/{seatId}")
    @RequireAuth(roles = {"ADMIN"})
    public ResponseEntity<ApiResponse<List<ReservationResponse>>> getAllReservationsByConcertIdAndSeatId(
            @PathVariable("concertId") Long concertId,
            @PathVariable("seatId") Long seatId) {
        List<ReservationResponse> responses = reservationService.getReservationsByConcertAndSeat(concertId, seatId);
        return ResponseEntity.ok(ApiResponse.success(responses));
    }
}
