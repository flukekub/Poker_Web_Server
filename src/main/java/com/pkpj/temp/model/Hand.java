package com.pkpj.temp.model;

import com.pkpj.temp.constant.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Data
public class Hand {
  // Will be set after persisting
    private Long tableId;
    private Deck deck;
    private List<Card> communityCards;
    private BigDecimal potSize;
    private GameState status;
    private BettingRound bettingRound;
    private int currentActorIndex;
    private List<Player> players;

    public Hand(Long tableId) {
        this.tableId = tableId;
        this.deck = new Deck();
        this.communityCards = new ArrayList<>();
        this.potSize = BigDecimal.ZERO;
        this.status = GameState.ACTIVE;
        this.bettingRound = BettingRound.PRE_FLOP;
        this.currentActorIndex = 0;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void addToPot(BigDecimal amount) {
        this.potSize = this.potSize.add(amount);
    }

    public void nextPlayer() {
        this.currentActorIndex = (this.currentActorIndex + 1) % players.size();
    }

    public void addHoleCardsToAllPlayers() {
        for (Player player : players) {
            List<Card> drawCards = deck.draw(2);
            player.receiveHoleCards(drawCards);
        }
    }
    public void assignPositions(int dealerIndex) {

        players.sort(Comparator.comparing(Player::getSeatNumber));
        players.forEach(p -> p.setPosition(null));

        int n = players.size();
        // Heads-up
        if (n == 2) {
            players.get(dealerIndex).setPosition(Position.SB);
            players.get((dealerIndex + 1) % n).setPosition(Position.BB);
            return;
        }

        players.get(dealerIndex).setPosition(Position.BTN);
        players.get((dealerIndex + 1) % n).setPosition(Position.SB);
        players.get((dealerIndex + 2) % n).setPosition(Position.BB);

        Position[] remaining = switch (n) {
            case 3 -> new Position[]{};
            case 4 -> new Position[]{ Position.UTG };
            case 5 -> new Position[]{ Position.UTG, Position.CO };
            case 6 -> new Position[]{ Position.UTG, Position.HJ, Position.CO };
            case 7 -> new Position[]{ Position.UTG, Position.UTG1, Position.HJ, Position.CO };
            case 8 -> new Position[]{ Position.UTG, Position.UTG1, Position.LJ, Position.HJ, Position.CO };
            case 9 -> new Position[]{ Position.UTG, Position.UTG1, Position.UTG2, Position.LJ, Position.HJ, Position.CO };
            default -> throw new IllegalArgumentException("Unsupported player count: " + n);
        };

        for (int i = 0; i < remaining.length; i++) {
            players.get((dealerIndex + 3 + i) % n)
                    .setPosition(remaining[i]);
        }
    }
    public void blindPayments(BigDecimal smallBlindAmount, BigDecimal bigBlindAmount) {
        Player sbPlayer = players.stream()
                .filter(p -> p.getPosition() == Position.SB)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Small Blind player found"));

        Player bbPlayer = players.stream()
                .filter(p -> p.getPosition() == Position.BB)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No Big Blind player found"));

        sbPlayer.bet(smallBlindAmount);
        bbPlayer.bet(bigBlindAmount);

        this.addToPot(smallBlindAmount.add(bigBlindAmount));
    }
}
