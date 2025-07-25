package com.reservation.reserve.reserve.service;

import com.reservation.reserve.reserve.domain.*;
import com.reservation.reserve.reserve.repository.ConcertRepository;
import com.reservation.reserve.reserve.repository.ReservationRepository;
import com.reservation.reserve.reserve.repository.SeatRepository;
import com.reservation.reserve.reserve.repository.VenueRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@Service
@RequiredArgsConstructor
public class InitService {

    private final ReservationRepository reservationRepository;
    private final ConcertRepository concertRepository;
    private final SeatRepository seatRepository;
    private final VenueRepository venueRepository;

//    @PostConstruct
//    @Transactional
    public void init() {
        // 데이터가 이미 있는지 확인
        if (venueRepository.count() > 0) {
            System.out.println("초기 데이터가 이미 존재합니다.");
            return;
        }

        // 1. 공연장 3개 생성
        List<VenueEntity> venues = createVenues();
        venueRepository.saveAll(venues);

        // 2. 좌석 10개 생성 (각 공연장마다)
        List<SeatEntity> seats = createSeats(venues);
        seatRepository.saveAll(seats);

        // 3. 콘서트 10개 생성
        List<ConcertEntity> concerts = createConcerts(venues);
        concertRepository.saveAll(concerts);

        // 4. 예약 10개 생성
        List<ReservationEntity> reservations = createReservations(concerts, seats);
        reservationRepository.saveAll(reservations);

        System.out.println("초기화 작업이 완료되었습니다.");
        System.out.println("생성된 데이터: 공연장 " + venues.size() + "개, 좌석 " + seats.size() + "개, 콘서트 " + concerts.size() + "개, 예약 " + reservations.size() + "개");
    }

    private List<VenueEntity> createVenues() {
        List<VenueEntity> venues = new ArrayList<>();

        venues.add(VenueEntity.builder()
                .name("올림픽공원 체조경기장")
                .totalCapacity(10000)
                .address(AddressVO.builder()
                        .postalCode("05540")
                        .province("서울특별시")
                        .city("송파구")
                        .district("방이동")
                        .streetAddress("올림픽로 424")
                        .buildingName("올림픽공원 체조경기장")
                        .build())
                .build());

        venues.add(VenueEntity.builder()
                .name("KSPO DOME")
                .totalCapacity(15000)
                .address(AddressVO.builder()
                        .postalCode("05540")
                        .province("서울특별시")
                        .city("송파구")
                        .district("방이동")
                        .streetAddress("올림픽로 424")
                        .buildingName("KSPO DOME")
                        .build())
                .build());

        venues.add(VenueEntity.builder()
                .name("고척 스카이돔")
                .totalCapacity(20000)
                .address(AddressVO.builder()
                        .postalCode("07807")
                        .province("서울특별시")
                        .city("구로구")
                        .district("고척동")
                        .streetAddress("경인로 430")
                        .buildingName("고척 스카이돔")
                        .build())
                .build());

        return venues;
    }

    private List<SeatEntity> createSeats(List<VenueEntity> venues) {
        List<SeatEntity> seats = new ArrayList<>();

        for (int venueIdx = 0; venueIdx < venues.size(); venueIdx++) {
            VenueEntity venue = venues.get(venueIdx);

            for (int i = 1; i <= 10; i++) {
                String section = i <= 5 ? "VIP" : "일반";
                long price = i <= 5 ? 150000L : 80000L;
                String grade = i <= 3 ? "S" : i <= 7 ? "A" : "B";

                SeatEntity seat = SeatEntity.builder()
                        .seatNumber(String.format("%s-%d", section.equals("VIP") ? "V" : "G", i))
                        .section(section)
                        .grade(grade)
                        .price(price)
                        .build();

                seat.addVenue(venue);
                seats.add(seat);
            }
        }

        return seats;
    }

    private List<ConcertEntity> createConcerts(List<VenueEntity> venues) {
        List<ConcertEntity> concerts = new ArrayList<>();
        String[] concertNames = {
            "BTS 월드투어 서울 공연",
            "아이유 콘서트 '더 골든 아워'",
            "뉴진스 1st 콘서트",
            "임영웅 전국투어 서울 공연",
            "르세라핌 첫 단독 콘서트",
            "세븐틴 월드투어",
            "스트레이키즈 도미네이트 투어",
            "에스파 2025 콘서트",
            "엔시티 드림 투어",
            "아일릿 데뷔 콘서트"
        };

        LocalDateTime baseDate = LocalDateTime.now().plusDays(30);

        for (int i = 0; i < 10; i++) {
            VenueEntity venue = venues.get(i % venues.size());

            ConcertEntity concert = ConcertEntity.builder()
                    .name(concertNames[i])
                    .date(baseDate.plusDays(i * 7).plusHours(19)) // 매주 토요일 오후 7시
                    .build();

            concert.addVenue(venue);
            concerts.add(concert);
        }

        return concerts;
    }

    private List<ReservationEntity> createReservations(List<ConcertEntity> concerts, List<SeatEntity> seats) {
        List<ReservationEntity> reservations = new ArrayList<>();
        String[] emails = {
            "user1@example.com", "user2@example.com", "user3@example.com",
            "user4@example.com", "user5@example.com", "user6@example.com",
            "user7@example.com", "user8@example.com", "user9@example.com",
            "user10@example.com"
        };

        StatusEnum[] statuses = {
            StatusEnum.CONFIRMED, StatusEnum.CONFIRMED, StatusEnum.TEMP_RESERVED,
            StatusEnum.CONFIRMED, StatusEnum.CANCELLED, StatusEnum.CONFIRMED,
            StatusEnum.TEMP_RESERVED, StatusEnum.EXPIRED, StatusEnum.CONFIRMED,
            StatusEnum.TEMP_RESERVED
        };

        for (int i = 0; i < 10; i++) {
            ConcertEntity concert = concerts.get(i);
            SeatEntity seat = seats.get(i);

            ReservationEntity reservation = ReservationEntity.builder()
                    .reserverEmail(emails[i])
                    .status(statuses[i])
                    .build();

            reservation.addConcert(concert);
            reservation.addSeat(seat);

            // TEMP_RESERVED 상태인 경우 만료 시간 설정
            if (statuses[i] == StatusEnum.TEMP_RESERVED) {
                reservation.setExpirationTime(5); // 5분 후 만료
            }

            reservations.add(reservation);
        }

        return reservations;
    }
}
