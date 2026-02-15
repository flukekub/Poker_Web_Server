package com.pkpj.temp.entities;

import com.pkpj.temp.constant.TableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "table_players")
public class TablePlayer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_player_id")
    private Long tablePlayerId;

    // --- Foreign Key Configuration ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false) // Defines the foreign key column
    private Tables table;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // Defines the foreign key column
    private User user;


    @Column(name = "stax")
    private BigDecimal stax;

    @Column( name = "seat_number")
    private Integer seatNumber;

    @Column( name = "joined_at")
    private Date joinedAt;

    @Column( name = "is_sitting")
    private Boolean isSitting;

    @Enumerated(EnumType.STRING)
    @Column( name = "table_status")
    private TableStatus tableStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_table_id")
    private GameTable gameTable;
}
