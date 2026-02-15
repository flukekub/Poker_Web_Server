package com.pkpj.temp.controller;

import com.pkpj.temp.dtos.games.HandDto;
import com.pkpj.temp.dtos.games.StartGameRequest;
import com.pkpj.temp.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


@Controller
public class GameController {
    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

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
