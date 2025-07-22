package com.reservation.reserve.reserve.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;
    private String section;
    private String grade;

    @OneToMany(mappedBy = "seat")
    private List<ReservationEntity> reservations = new ArrayList<>();
}
