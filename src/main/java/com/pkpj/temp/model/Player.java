package com.pkpj.temp.model;

import com.pkpj.temp.constant.BettingRound;
import com.pkpj.temp.constant.Card;
import com.pkpj.temp.constant.Position;
import com.pkpj.temp.entities.TablePlayer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Long playerId;
    private int seatNumber;
    private List<Card> holeCards;
    private Position position; // Button, SMALL_BLIND, BIG_BLIND, UTG, etc.
    private BigDecimal stax;
    private boolean folded;

    public void bet(BigDecimal amount) {
        this.stax = this.stax.subtract(amount);
    }

    public void fold() {
        this.folded = true;
    }

    public static Player from(TablePlayer entity) {
        Player p = new Player();
        p.playerId = entity.getUser().getId();
        p.seatNumber = entity.getSeatNumber();
        p.stax = entity.getStax();
        return p;
    }

    public void receiveHoleCards(List<Card> cards) {
        this.holeCards = cards;
    }


}
