package com.pkpj.temp.entities;

import com.pkpj.temp.constant.BettingRound;
import com.pkpj.temp.constant.GameState;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hands")
@Getter
@Setter
public class HandEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hand_id")
    private Long handId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private Tables table;

    @Column(columnDefinition = "TEXT", name = "community_cards")
    private String communityCards; // Stored as JSON string: ["Ah","Kd","10s"]

    @Column(nullable = false, name = "pot_size")
    private BigDecimal potSize;

    @Column(columnDefinition = "TEXT", name = "players")
    private String players; // Stored as JSON string

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private GameState status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "betting_round")
    private BettingRound bettingRound;

    @Column(name = "current_actor_index", nullable = false)
    private int currentActorIndex;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column( nullable = false, name = "completed_at")
    private LocalDateTime completedAt;

    @OneToMany(mappedBy = "hand", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerActionEntity> playerActions = new ArrayList<>();
}
