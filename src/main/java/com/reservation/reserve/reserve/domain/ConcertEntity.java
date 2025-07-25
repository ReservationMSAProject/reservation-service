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
@Table(name = "concert", indexes = {
    @Index(name = "idx_concert_date", columnList = "date")
})
public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private LocalDateTime date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private VenueEntity venue;

    @OneToMany(mappedBy = "concert", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ReservationEntity> reservations = new ArrayList<>();

    // 연관관계 편의 메서드
    public void addVenue(VenueEntity venue) {
        this.venue = venue;
        if (venue != null && !venue.getConcerts().contains(this)) {
            venue.getConcerts().add(this);
        }
    }
}
