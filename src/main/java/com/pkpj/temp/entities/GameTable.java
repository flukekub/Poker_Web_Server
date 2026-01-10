package com.pkpj.temp.entities;

import com.pkpj.temp.constant.Deck;
import jakarta.persistence.*; // IMPT: Use jakarta for @Id, @Entity, etc.
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Game_table")
public class GameTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="game_table_id")
    private Long gameTableId;


    @OneToOne(mappedBy = "gameTable", fetch = FetchType.LAZY)
    private Tables table;


    // --- The "Real" Deck (Java Object) ---
    // @Transient means "Ignore this field for SQL"
    @Transient
    private Deck deck;

    // --- The "Stored" Deck (String) ---
    // This is what actually gets saved to the database row
    @Column(name = "deck_state", columnDefinition = "TEXT")
    private String deckState;

    // private GameStatus status;

    // --- Lifecycle Method 1: Loading (Read) ---
    // Runs automatically after Hibernate reads the row from DB
    @PostLoad
    public void convertStringToDeck() {
        // Case 1: We have a saved state (even if it's an empty string representing 0 cards)
        if (this.deckState != null) {
            this.deck = new Deck(this.deckState);
        }
        // Case 2: This is a brand new row in DB that hasn't been initialized yet
        else {
            this.deck = new Deck();
            this.deck.shuffle();
        }
    }

    // --- Lifecycle Method 2: Saving (Write) ---
    // Runs automatically before Hibernate inserts or updates the row
    @PrePersist
    @PreUpdate
    public void convertDeckToString() {
        if (this.deck != null) {
            // Compress the deck object into a string for storage
            this.deckState = this.deck.toStateString();
        }
    }

    // Inverse mapping for Hands
    @OneToMany(mappedBy = "gameTable", cascade = CascadeType.ALL)
    private List<HandEntity> hands = new ArrayList<>();

    // --- Inverse Mapping (Optional but recommended) ---
    // mappedBy refers to the "tableId" field in the TablePlayer class
    @OneToMany(mappedBy = "gameTable", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TablePlayer> tablePlayers;
}