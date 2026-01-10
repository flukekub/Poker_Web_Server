package com.pkpj.temp.controller;


import com.pkpj.temp.dtos.TableDto;
import com.pkpj.temp.entities.Tables;
import com.pkpj.temp.services.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class TablesController {

    @Autowired
    private TableService tableService;

    private static final Logger logger = LoggerFactory.getLogger(TablesController.class);

    @PostMapping("/api/tables")
    public ResponseEntity<?> createTable(@RequestBody TableDto tablesReq) {
        try {
            logger.info("Creating table with request: {}", tablesReq);
            TableDto createdTable = tableService.createTable(tablesReq);
            logger.info("Table created successfully with ID: {}", createdTable);
            return ResponseEntity.ok(createdTable);
        } catch (Exception e) {
            logger.error("Error creating table: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", e.getMessage())); // Internal Server Error
        }
    }

    @GetMapping("/api/tables/type")
    public ResponseEntity<?> getTableByGameType(@RequestParam String gameType) {
        try {
            logger.info("Fetching tables by game type: {}", gameType);
            if (gameType == null || gameType.isEmpty()) {
                logger.warn("GameType parameter is null or empty");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Bad Request", "message", "gameType is required")); // Bad Request
            }
            TableDto[] tables = tableService.getTableByGameType(gameType);
            logger.info("Retrieved {} tables for game type: {}", tables.length, gameType);
            return ResponseEntity.ok(tables);
        } catch (Exception e) {
            logger.error("Error fetching tables by game type: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", e.getMessage())); // Internal Server Error
        }
    }
    @GetMapping("/api/table/id")
    public ResponseEntity<?> getTableById(@RequestParam Long tableId) {
        try {
            logger.info("Fetching table by ID: {}", tableId);
            if (tableId == null) {
                logger.warn("TableId parameter is null");
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Bad Request", "message", "tableId is required")); // Bad Request
            }
            TableDto table = tableService.getTableById(tableId);
            logger.info("Retrieved table with ID: {}", tableId);
            return ResponseEntity.ok(table);
        } catch (Exception e) {
            logger.error("Error fetching table by ID: ", e);
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Server error", "message", e.getMessage())); // Internal Server Error
        }
    }
}
