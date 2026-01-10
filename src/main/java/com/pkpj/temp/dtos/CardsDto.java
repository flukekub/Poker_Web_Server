package com.pkpj.temp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;

@Value
public class CardsDto {
    List<CardDto> cards;
}
