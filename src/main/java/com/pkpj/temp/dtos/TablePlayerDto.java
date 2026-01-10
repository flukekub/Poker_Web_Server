package com.pkpj.temp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TablePlayerDto {
    private Long tablePlayerId;
    private Long userId;
    private Long gameTableId;
    private Long tableId;
    private BigDecimal stax;
    private Integer seatNumber;
    private Date joinedAt;
    private Boolean isSitting;
    private String profileImageUrl;
    private String username;
}
