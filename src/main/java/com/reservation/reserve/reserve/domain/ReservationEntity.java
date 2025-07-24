package com.reservation.reserve.reserve.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id")
    private SeatEntity seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id")
    private ConcertEntity concert;

    @Column(name = "reserver_email")
    private String reserverEmail;

    @CreatedDate
    private LocalDateTime createAt;

    @LastModifiedDate
    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private StatusEnum status;

    public void addSeat(SeatEntity seat) {
        this.seat = seat;
        if (seat != null && !seat.getReservations().contains(this)) {
            seat.getReservations().add(this);
        }
    }

    public void addConcert(ConcertEntity concert) {
        this.concert = concert;
        if (concert != null && !concert.getReservations().contains(this)) {
            concert.getReservations().add(this);
        }
    }

    public void updateStatus(StatusEnum status) {
        this.status = status;
    }
}
