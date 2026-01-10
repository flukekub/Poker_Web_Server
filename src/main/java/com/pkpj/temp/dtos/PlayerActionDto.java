package com.pkpj.temp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerActionDto {
    private Long handId;
    private Long playerId;
    private String actionType;  // FOLD, CALL, RAISE, BET,
    private String amount;
    private String bettingRound;
    private int sequenceNumber;
}
