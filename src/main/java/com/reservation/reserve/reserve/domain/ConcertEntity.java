package com.reservation.reserve.reserve.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date")
    private LocalDateTime date;

    @Column(name="location")
    private String location;

    @OneToMany(mappedBy = "concert")
    private List<ReservationEntity> reservations = new ArrayList<>();
}
