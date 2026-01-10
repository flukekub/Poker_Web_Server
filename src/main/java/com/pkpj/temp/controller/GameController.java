package com.pkpj.temp.controller;

import com.pkpj.temp.dtos.CardDto;
import com.pkpj.temp.dtos.GameTableDto;
import com.pkpj.temp.dtos.TableDto;
import com.pkpj.temp.dtos.TablePlayerDto;
import com.pkpj.temp.services.GameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    GameService gameService;

    @PostMapping("/api/games/tablePlayer")
    public ResponseEntity<?> createTable(@RequestBody TablePlayerDto tablePlayerReq) {
        try {
            TablePlayerDto createdTablePlayer = gameService.createTablePlayer(tablePlayerReq);
            logger.info("TablePlayer created successfully: {}", createdTablePlayer);
            return ResponseEntity.ok(createdTablePlayer);
        } catch (Exception e) {
            logger.error("Failed to create TablePlayer: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", e.getMessage())); // Internal Server Error
        }
    }

    @GetMapping("/api/games/tablePlayer/")
    public ResponseEntity<?> getTablePlayerByTableId(@RequestParam Long tableId) {
        try {
            if (tableId == null) {
                logger.warn("tableId is missing in the request");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Bad Request", "message", "tableId is required")); // Bad Request
            }
            List<TablePlayerDto> tablePlayers = gameService.getTablePlayerByTableId(tableId);
            logger.info("Fetched {} TablePlayers for tableId {}", tablePlayers.size(), tableId);
            return ResponseEntity.ok(tablePlayers);
        } catch (Exception e) {
            logger.error("Failed to fetch TablePlayers for tableId {}: {}", tableId, e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", e.getMessage())); // Internal Server Error
        }
    }

    @PostMapping("/api/games/gameTable/")
    public ResponseEntity<?> createGameTable(@RequestBody GameTableDto gameTableDto) {
            GameTableDto createdGameTable = gameService.createGameTable(gameTableDto);
            logger.info("GameTable created successfully: {}", createdGameTable);
            return ResponseEntity.ok(createdGameTable);
    }



}
