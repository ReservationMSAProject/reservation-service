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
@Table(name = "seat", indexes = {
    @Index(name = "idx_seat_venue", columnList = "venue_id"),
    @Index(name = "idx_seat_number_section", columnList = "seat_number, section")
})
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;

    @Column(name = "seat_number", nullable = false)
    private String seatNumber;

    @Column(name = "section")
    private String section;

    @Column(name = "grade")
    private String grade;

    @Column(name = "price", nullable = false)
    private long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private VenueEntity venue;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReservationEntity> reservations = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addVenue(VenueEntity venue) {
        this.venue = venue;
        if (venue != null && !venue.getSeats().contains(this)) {
            venue.getSeats().add(this);
        }
    }
}
