package com.reservation.reserve.reserve.service;

import com.reservation.reserve.reserve.domain.*;
import com.reservation.reserve.reserve.repository.ConcertRepository;
import com.reservation.reserve.reserve.repository.ReservationRepository;
import com.reservation.reserve.reserve.repository.SeatRepository;
import com.reservation.reserve.reserve.repository.VenueRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        if (venueRepository.count() > 0) {
            System.out.println("초기 데이터가 이미 존재합니다.");
            return;
        }

        // 1. 공연장 3개 생성
        List<VenueEntity> venues = createVenues();
        venueRepository.saveAll(venues);

        // 2. 각 공연장마다 좌석 30개 생성
        List<SeatEntity> seats = createSeats(venues);
        seatRepository.saveAll(seats);

        // 3. 콘서트 3개 생성 (각기 다른 공연장에 할당)
        List<ConcertEntity> concerts = createConcerts(venues);
        concertRepository.saveAll(concerts);

        // 4. 각 콘서트별로 5~10개 예약 생성
        List<ReservationEntity> reservations = createReservations(concerts, seats);
        reservationRepository.saveAll(reservations);

        System.out.println("초기화 작업이 완료되었습니다.");
        System.out.println("생성된 데이터: 공연장 " + venues.size() + "개, 좌석 " + seats.size() + "개, 콘서트 " + concerts.size() + "개, 예약 " + reservations.size() + "개");
    }

    private List<VenueEntity> createVenues() {
        List<VenueEntity> venues = new ArrayList<>();
        venues.add(VenueEntity.builder()
                .name("올림픽공원 체조경기장")
                .totalCapacity(30000)
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
                .totalCapacity(30000)
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
                .totalCapacity(30000)
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
        for (VenueEntity venue : venues) {
            for (int i = 1; i <= 30; i++) {
                String section = i <= 10 ? "VIP" : i <= 20 ? "R" : "S";
                long price = i <= 10 ? 150000L : i <= 20 ? 100000L : 70000L;
                String grade = i <= 10 ? "S" : i <= 20 ? "A" : "B";
                boolean active = i % 7 != 0;

                SeatEntity seat = SeatEntity.builder()
                        .seatNumber(String.format("%s-%02d", section, i))
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
                "뉴진스 1st 콘서트"
        };

        LocalDateTime baseDate = LocalDateTime.now().plusDays(30);
        for (int i = 0; i < 3; i++) {
            ConcertEntity concert = ConcertEntity.builder()
                    .name(concertNames[i])
                    .date(baseDate.plusDays(i * 7).plusHours(19))
                    .build();
            concert.addVenue(venues.get(i));
            concerts.add(concert);
        }
        return concerts;
    }

    private List<ReservationEntity> createReservations(List<ConcertEntity> concerts, List<SeatEntity> allSeats) {
        List<ReservationEntity> reservations = new ArrayList<>();
        Random random = new Random();
        int emailSeq = 1;

        for (ConcertEntity concert : concerts) {
            List<SeatEntity> venueSeats = allSeats.stream()
                    .filter(seat -> seat.getVenue().getName().equals(concert.getVenue().getName()))
                    .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

            Collections.shuffle(venueSeats);

            int reservationCount = 5 + random.nextInt(6);
            for (int i = 0; i < Math.min(reservationCount, venueSeats.size()); i++) {
                SeatEntity seat = venueSeats.get(i);
                StatusEnum status = StatusEnum.values()[random.nextInt(StatusEnum.values().length)];
                String email = "user" + (emailSeq++) + "@example.com";

                ReservationEntity reservation = ReservationEntity.builder()
                        .reserverEmail(email)
                        .status(status)
                        .build();

                reservation.addConcert(concert);
                reservation.addSeat(seat);


                if (status == StatusEnum.TEMP_RESERVED) {
                    reservation.setExpirationTime(5);
                }
                reservations.add(reservation);
            }
        }
        return reservations;
    }
}