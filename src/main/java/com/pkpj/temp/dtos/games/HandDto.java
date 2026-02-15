package com.pkpj.temp.dtos.games;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HandDto {
    private Long tableId;
    private BigDecimal potSize;
    private String status;
    private String bettingRound;
    private int buttonSeatIndex;
    private LocalDateTime createdAt;
}