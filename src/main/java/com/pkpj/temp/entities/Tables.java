package com.pkpj.temp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table( name = "tables")
public class Tables {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "table_id")
    private Long tableId;

    @Column( name = "table_name")
    private String tableName; // Name of the poker table

    @Column( name = "description")
    private String description;

    @Column(name = "game_type")
    private String gameType; // Game type, e.g., NLHE, ShortDeck

    @Column(name = "min_buy_in")
    private BigDecimal minBuyIn; // Minimum buy-in amount

    @Column(name = "max_buy_in")
    private BigDecimal maxBuyIn; // Maximum buy-in amount

    @Column(name = "max_players")
    private Integer maxPlayers; // Maximum number of players at the table

    @Column(name = "current_players")
    private Integer currentPlayers; // Current number of players at the table

    @Column(name = "is_active")
    private Boolean isActive; // Whether the table is currently active

    @Column( name = "created_at")
    private Date createdAt; // Timestamp when the table was created

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<TablePlayer> players = new ArrayList<>();
    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HandEntity> hands = new ArrayList<>();

}
