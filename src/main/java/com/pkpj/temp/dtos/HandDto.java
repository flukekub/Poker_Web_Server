package com.pkpj.temp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

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