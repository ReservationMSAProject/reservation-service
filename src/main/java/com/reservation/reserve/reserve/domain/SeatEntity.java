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

    @Column(name = "seat_number")
    private String seatNumber;

    @Column(name = "section")
    private String section;

    @Column(name = "grade")
    private String grade;

    @Column(name = "price")
    private String price;

    @OneToMany(mappedBy = "seat")
    private List<ReservationEntity> reservations = new ArrayList<>();
}
