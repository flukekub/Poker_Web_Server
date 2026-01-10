package com.pkpj.temp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartGameRequest {
    private Long tableId;
    private int dealerSeatNumber;
    private BigDecimal smallBlindAmount;
    private BigDecimal bigBlindAmount;
}
