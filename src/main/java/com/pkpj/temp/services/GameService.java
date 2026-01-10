package com.pkpj.temp.services;

import com.pkpj.temp.constant.*;
import com.pkpj.temp.dtos.*;
import com.pkpj.temp.entities.*;
import com.pkpj.temp.manager.HandManager;
import com.pkpj.temp.model.Hand;
import com.pkpj.temp.model.Player;
import com.pkpj.temp.model.PlayerAction;
import com.pkpj.temp.repositories.GameTableRepository;
import com.pkpj.temp.repositories.TablePlayerRepository;
import com.pkpj.temp.repositories.TablesRepository;
import com.pkpj.temp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameService {

    private final UserRepository userRepository;
    private final TablesRepository tablesRepository;
    private final TablePlayerRepository tablePlayerRepository;
    private final GameTableRepository gameTableRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final HandService handService;
    private final HandManager handManager;


    public TablePlayerDto createTablePlayer(TablePlayerDto tablePlayerReq) {
        TablePlayer tablePlayer = new TablePlayer();
        Tables table = tablesRepository.findById(tablePlayerReq.getTableId()).orElse(null);
        tablePlayer.setTable(table);
        User user = userRepository.findById(tablePlayerReq.getUserId()).orElse(null);
        tablePlayer.setUser(user);
        tablePlayer.setStax(tablePlayerReq.getStax());
        tablePlayer.setSeatNumber(tablePlayerReq.getSeatNumber());
        tablePlayer.setIsSitting(true);
        tablePlayer.setJoinedAt(new java.util.Date());
        tablePlayer.setTableStatus(TableStatus.WAITING);
        // Save tablePlayer to the database (assuming you have a TablePlayerRepository)
        tablePlayerRepository.save(tablePlayer);

        NotificationPayload payload = new NotificationPayload("TABLE"+tablePlayer.getTable().getTableId()+"_JOINED", "New player joined the table");
        messagingTemplate.convertAndSend("/topic/notifications", payload);

        return new TablePlayerDto(
                tablePlayer.getTablePlayerId(),        // tablePlayerId
                tablePlayer.getUser().getId(),       // userId
                tablePlayer.getTable().getGameTable().getGameTableId(),
                tablePlayer.getTable().getTableId(), // tableId
                tablePlayer.getStax(),                 // stax
                tablePlayer.getSeatNumber(),                                  // seatNumber (not available)
                tablePlayer.getJoinedAt(),             // joinedAt (Date, not String)
                tablePlayer.getIsSitting(),             // isSitting
                tablePlayer.getUser().getProfileImageUrl(),
                tablePlayer.getUser().getName()
        );
    }
    public List<TablePlayerDto> getTablePlayerByTableId(Long tableId) {
        List<TablePlayer> tablePlayers = tablePlayerRepository.findByTable_TableId(tableId);

        return tablePlayers.stream().map(tablePlayer -> new TablePlayerDto(
                tablePlayer.getTablePlayerId(),
                tablePlayer.getUser().getId(),
                tablePlayer.getTable().getGameTable().getGameTableId(),
                tablePlayer.getTable().getTableId(),
                tablePlayer.getStax(),
                tablePlayer.getSeatNumber(),
                tablePlayer.getJoinedAt(),
                tablePlayer.getIsSitting(),
                tablePlayer.getUser().getProfileImageUrl(),
                tablePlayer.getUser().getName()
        )).toList();
    }

    public GameTableDto createGameTable(GameTableDto gameTableDto) {
        GameTable gameTable = new GameTable();
        Tables table = tablesRepository.findById(gameTableDto.getTableId()).orElse(null);
        gameTable.setTable(table);
        gameTableRepository.save(gameTable);
        return new GameTableDto(gameTable.getGameTableId(),gameTable.getTable().getTableId(),0);
    }

    @Transactional
    public List<Card> drawCard(Long gameTableId, int cardAmount) {
        // Find the game table by table ID
        GameTable gameTable = gameTableRepository.findById(gameTableId)
                .orElseThrow(() -> new RuntimeException("Game table not found"));

        // Get the current deck from game table
        Deck deck = gameTable.getDeck();

        if (deck == null) {
            // Initialize a new shuffled deck if none exists
            deck = new Deck();
            deck.shuffle();
            gameTable.setDeck(deck);
        }

        if (deck.isEmpty()) {
            throw new RuntimeException("Deck is empty");
        }

        // Draw the top card (remove from deck)
        List<Card> drawnCards = new ArrayList<>();
        for (int i = 0; i < cardAmount ; i++) {
            Card card = deck.draw();
            drawnCards.add(card);
        }

        // Update the deck in the game table
        gameTable.setDeck(deck);

        // We must manually sync the Object to the String BEFORE saving.
        // This tells Hibernate "Hey, the deckState field has changed!"
        gameTable.convertDeckToString();

        // Save the updated game table
        gameTableRepository.save(gameTable);

        // Return the drawn card
        return drawnCards;
    }

    @Transactional
    public void resetDeck(Long gameTableId) {
        // Find the game table by ID
        GameTable gameTable = gameTableRepository.findById(gameTableId)
                .orElseThrow(() -> new RuntimeException("Game table not found"));

        // Create a new shuffled deck
        Deck newDeck = new Deck();
        newDeck.shuffle();

        // Set the new deck to the game table
        gameTable.setDeck(newDeck);

        // If you don't do this, Hibernate won't detect the change!
        gameTable.convertDeckToString();

        // Save the updated game table
        gameTableRepository.save(gameTable);

        // Send notification to topic subscribers
        NotificationPayload payload = new NotificationPayload(
                "TABLE" + gameTable.getTable().getTableId() + "_DECK_RESET",
                "Deck has been reset"
        );
        messagingTemplate.convertAndSend("/topic/notifications", payload);
    }

    @Transactional
    public HandDto startGame(Long tableId, int dealerSeatNumber, BigDecimal smallBlindAmount, BigDecimal bigBlindAmount) {
        // Validate table exists
        Tables table = tablesRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        // Create Hand POJO (game logic model)
        Hand hand = new Hand(tableId);

        // ... card dealing code ...
        List<TablePlayer> tablePlayers = table.getPlayers();
        List<Player> domainPlayers = tablePlayers.stream()
                .map(Player::from)
                .toList();
        hand.assignPositions(dealerSeatNumber);
        hand.setPlayers(domainPlayers);
        hand.addHoleCardsToAllPlayers();
        hand.blindPayments(smallBlindAmount, bigBlindAmount);
        handManager.put(tableId, hand);

        return convertToDto(hand);
    }

    private HandDto convertToDto(Hand hand) {
        HandDto dto = new HandDto();
        dto.setTableId(hand.getTableId());
        dto.setBettingRound(hand.getBettingRound().name());
        dto.setPotSize(hand.getPotSize());
        dto.setStatus(hand.getStatus().name());
        dto.setBettingRound(hand.getBettingRound().name());
        dto.setButtonSeatIndex(hand.getCurrentActorIndex());
        dto.setCreatedAt(java.time.LocalDateTime.now());
        return dto;
    }
}
