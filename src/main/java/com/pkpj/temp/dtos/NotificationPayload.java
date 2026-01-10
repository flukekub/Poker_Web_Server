package com.pkpj.temp.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationPayload {
    private String type;    // e.g., "TABLE_UPDATED", "USER_JOINED"
    private String content; // e.g., "Table #123 has a new player"
}
