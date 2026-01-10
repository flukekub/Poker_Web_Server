package com.pkpj.temp.controller;

import com.pkpj.temp.constant.Card;
import com.pkpj.temp.dtos.*;
import com.pkpj.temp.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class GameWsController {
    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GameWsController.class);

    public CardsDto toCardsDto(List<Card> cards) {
        List<CardDto> cardsDto = cards.stream()
                .map(card -> new CardDto(
                        card.getSuit().toString(),
                        card.getRank().toString()
                ))
                .toList();
        return new CardsDto(cardsDto);
    }

    @MessageMapping("/game/draw")
    public void handleDraw(@Payload GameTableDto gameTableDto) {
        List<Card> cards = gameService.drawCard(gameTableDto.getGameTableId(), gameTableDto.getCardAmount());
        String dest = "/topic/game/" + gameTableDto.getGameTableId() + "/card";
        CardsDto cardsDto = toCardsDto(cards);
        messagingTemplate.convertAndSend(dest, cardsDto);
        logger.info("Sent drawn card to destination {}: {}", dest, cardsDto);
    }

    @MessageMapping("/game/resetDeck")
    public void handleResetDeck(@Payload GameTableDto gameTableDto) {
        gameService.resetDeck(gameTableDto.getGameTableId());
        String dest = "/topic/game/" + gameTableDto.getGameTableId() + "/deckReset";
        messagingTemplate.convertAndSend(dest, "Deck has been reset");
        logger.info("Deck reset for game table {}", gameTableDto.getGameTableId());
    }

    @MessageMapping("/game/start")
    public void handleStartGame(@Payload StartGameRequest request) {
        try {
            HandDto hand = gameService.startGame(request.getTableId(),request.getDealerSeatNumber(), request.getSmallBlindAmount(), request.getBigBlindAmount());
            String dest = "/topic/game/" + request.getTableId() + "/started";
            messagingTemplate.convertAndSend(dest, hand);
            logger.info("Game started for table {} with hand {}", request.getTableId(), hand);
        } catch (Exception e) {
            logger.error("Failed to start game: {}", e.getMessage());
            String errorDest = "/topic/game/" + request.getTableId() + "/error";
            messagingTemplate.convertAndSend(errorDest, "Failed to start game: " + e.getMessage());
        }
    }
}
