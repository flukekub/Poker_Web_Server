package com.pkpj.temp.entities;

import com.pkpj.temp.constant.BettingRound;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "player_actions")
@Getter
@Setter
public class PlayerActionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "player_action_id")
    private Long playerActionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hand_id", nullable = false)
    private HandEntity hand;

    @Column(nullable = false, name = "player_id")
    private Long playerId;

    @Column(nullable = false, name = "action_type")
    private String actionType; // FOLD, CHECK, CALL, RAISE, BET

    @Column(nullable = false, name = "amount")
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "betting_round")
    private BettingRound bettingRound;

    @Column(nullable = false, name = "timestamp")
    private LocalDateTime timestamp;

    @Column(nullable = false, name = "sequence_number")
    private int sequenceNumber;
}
