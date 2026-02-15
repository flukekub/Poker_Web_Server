package com.pkpj.temp.dtos.tables;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableDto {
    private Long tableId;
    private String tableName;
    private String description;
    private String gameType;
    private BigDecimal minBuyIn;
    private BigDecimal maxBuyIn;
    private Integer maxPlayers;
    private Integer currentPlayers;
    private Boolean isActive;
    private String createdAt; // Assuming this is a string representation of the date, adjust as
}
