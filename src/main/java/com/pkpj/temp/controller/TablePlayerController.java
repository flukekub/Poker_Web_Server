package com.pkpj.temp.controller;

import com.pkpj.temp.dtos.games.TablePlayerDto;
import com.pkpj.temp.services.GameService;
import com.pkpj.temp.services.TablePlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/games")
public class TablePlayerController {
    private static final Logger logger = LoggerFactory.getLogger(TablePlayerController.class);

    @Autowired
    TablePlayerService tablePlayerService;

    @PostMapping("/tablePlayer")
    public ResponseEntity<?> createTable(@RequestBody TablePlayerDto tablePlayerReq) {

        try {
            TablePlayerDto createdTablePlayer = tablePlayerService.createTablePlayer(tablePlayerReq);
            logger.info("TablePlayer created successfully: {}", createdTablePlayer);
            return ResponseEntity.ok(createdTablePlayer);
        } catch (Exception e) {
            logger.error("Failed to create TablePlayer: {}", e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", e.getMessage())); // Internal Server Error
        }
    }

    @GetMapping("/tablePlayer")
    public ResponseEntity<?> getTablePlayerByTableId(@RequestParam Long tableId) {
        try {
            if (tableId == null) {
                logger.warn("tableId is missing in the request");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Bad Request", "message", "tableId is required")); // Bad Request
            }
            List<TablePlayerDto> tablePlayers = tablePlayerService.getTablePlayerByTableId(tableId);
            logger.info("Fetched {} TablePlayers for tableId {}", tablePlayers.size(), tableId);
            return ResponseEntity.ok(tablePlayers);
        } catch (Exception e) {
            logger.error("Failed to fetch TablePlayers for tableId {}: {}", tableId, e.getMessage(), e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", e.getMessage())); // Internal Server Error
        }
    }
    @DeleteMapping("/tablePlayer")
    public ResponseEntity<?> deleteTablePlayer(@RequestParam Long tablePlayerId) {
        tablePlayerService.deleteTablePlayer(tablePlayerId);
        logger.info("Deleted TablePlayer with ID: {}", tablePlayerId);
        return ResponseEntity.ok("TablePlayer deleted successfully");
    }
}
