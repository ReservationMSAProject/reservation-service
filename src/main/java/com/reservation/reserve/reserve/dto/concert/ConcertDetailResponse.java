package com.reservation.reserve.reserve.dto.concert;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ConcertDetailResponse {
    private Long id;
    private String name;
    private LocalDateTime date;
    private VenueInfo venue;
    private List<SeatInfo> seats;

    @Getter
    @AllArgsConstructor
    public static class VenueInfo {
        private Long id;
        private String name;
        private String address;
    }

    @Getter
    @AllArgsConstructor
    public static class SeatInfo {
        private Long id;
        private String seatNumber;
        private String section;
        private String grade;
        private boolean active;
        private long price;
    }
}