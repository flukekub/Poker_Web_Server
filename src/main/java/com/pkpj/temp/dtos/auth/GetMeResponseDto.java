package com.pkpj.temp.dtos.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetMeResponseDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String profilePicture;
    private BigDecimal chips;
    private Date createdAt;
}
