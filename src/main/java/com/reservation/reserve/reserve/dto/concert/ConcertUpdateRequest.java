package com.reservation.reserve.reserve.dto.concert;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ConcertUpdateRequest {
    @NotBlank(message = "콘서트 이름은 필수입니다")
    private String name;
    
    @NotNull(message = "콘서트 날짜는 필수입니다")
    @Future(message = "콘서트 날짜는 미래여야 합니다")
    private LocalDateTime date;
}
