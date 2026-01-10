package com.pkpj.temp.constant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        // Loop through all Suits and Ranks to populate the deck
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
    }

    public Boolean isEmpty() {
        return this.cards.isEmpty();
    }

    public void shuffle() {
        Collections.shuffle(this.cards);
    }

    public Card draw() {
        if (cards.isEmpty()) {
            return null; // Or throw an exception
        }
        // Removes and returns the top card (index 0)
        return cards.removeFirst();
    }

    public List<Card> draw(int amount) {
        if (amount <= 0) {
            return new ArrayList<>();
        }

        if (amount > cards.size()) {
            amount = cards.size(); // Draw only available cards
        }

        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            drawnCards.add(cards.removeFirst());
        }

        return drawnCards;
    }

    public int size() {
        return cards.size();
    }

    // REQUIRED for @PrePersist
    public String toStateString() {
        if (cards == null || cards.isEmpty()) return "";
        // Convert list of cards to "H-TWO,S-K,D-TEN"
        return cards.stream()
                .map(card -> card.getSuit().name() + "-" + card.getRank().name())
                .collect(Collectors.joining(","));
    }

    // REQUIRED for @PostLoad
    public Deck(String stateString) {
        this.cards = new ArrayList<>();
        if (stateString != null && !stateString.isEmpty()) {
            String[] parts = stateString.split(",");
            for (String part : parts) {
                String[] cardData = part.split("-");
                // Convert string back to Enum
                Suit s = Suit.valueOf(cardData[0]);
                Rank r = Rank.valueOf(cardData[1]);
                this.cards.add(new Card(s, r));
            }
        }
    }
}
