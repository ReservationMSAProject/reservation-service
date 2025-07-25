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
@Table(name = "venue")
public class VenueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Embedded
    private AddressVO address;

    @Column(name = "total_capacity")
    private int totalCapacity;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SeatEntity> seats = new ArrayList<>();

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ConcertEntity> concerts = new ArrayList<>();
}

