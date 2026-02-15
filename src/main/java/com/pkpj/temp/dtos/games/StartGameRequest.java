package com.pkpj.temp.dtos.games;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartGameRequest {
    private Long tableId;
    private int dealerSeatNumber;
    private BigDecimal smallBlindAmount;
    private BigDecimal bigBlindAmount;
}
