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
@Table(name = "reservation", 
       uniqueConstraints = {
           @UniqueConstraint(name = "uk_concert_seat_active", 
                           columnNames = {"concert_id", "seat_id"})
       },
       indexes = {
           @Index(name = "idx_reservation_status", columnList = "status"),
           @Index(name = "idx_reservation_expires", columnList = "expires_at"),
           @Index(name = "idx_reservation_email", columnList = "reserver_email")
       })
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seat_id", nullable = false)
    private SeatEntity seat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_id", nullable = false)
    private ConcertEntity concert;

    @Column(name = "reserver_email", nullable = false)
    private String reserverEmail;

    @CreatedDate
    @Column(name = "created_at")
    private LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updateAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status;

    // 연관관계 편의 메서드
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

    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canBeConfirmed() {
        return status == StatusEnum.TEMP_RESERVED && !isExpired();
    }

    public void setExpirationTime(int minutes) {
        this.expiresAt = LocalDateTime.now().plusMinutes(minutes);
    }
}
