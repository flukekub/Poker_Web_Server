package com.pkpj.temp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameTableDto {
    private Long gameTableId;
    private Long tableId;
    private int cardAmount;
}
