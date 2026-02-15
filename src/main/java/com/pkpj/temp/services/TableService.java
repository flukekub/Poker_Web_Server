package com.pkpj.temp.services;

import com.pkpj.temp.constant.GameType;
import com.pkpj.temp.dtos.NotificationPayload;
import com.pkpj.temp.dtos.tables.TableDto;
import com.pkpj.temp.entities.Tables;
import com.pkpj.temp.repositories.TablesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class TableService {
    @Autowired
    private TablesRepository tablesRepository;

    @Autowired
    private GameService gameService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public TableDto createTable(TableDto tablesReq) {
        Tables tables = new Tables();

        tables.setTableName(tablesReq.getTableName());
        tables.setDescription( tablesReq.getDescription());
        tables.setGameType(GameType.valueOf(tablesReq.getGameType()).name());
        tables.setMinBuyIn(tablesReq.getMinBuyIn());
        tables.setMaxBuyIn(tablesReq.getMaxBuyIn());
        tables.setMaxPlayers(tablesReq.getMaxPlayers());
        tables.setCurrentPlayers(tablesReq.getCurrentPlayers());
        tables.setIsActive(true);
        tables.setCreatedAt(new java.util.Date()); // set current time

        tablesRepository.save(tables);

        NotificationPayload payload = new NotificationPayload("TABLE_UPDATED", "New table created");
        messagingTemplate.convertAndSend("/topic/notifications", payload);
        return new TableDto(
                tables.getTableId(),
                tables.getTableName(),          // tableName
                tables.getDescription(),
                tables.getGameType(),    // gameType (converted to String)
                tables.getMinBuyIn(),           // minBuyIn
                tables.getMaxBuyIn(),           // maxBuyIn
                tables.getMaxPlayers(),         // maxPlayers
                tables.getPlayers().size(),     // currentPlayers
                tables.getIsActive(),           // isActive
                tables.getCreatedAt().toString() // createdAt (converted to String)
        );
    }
    public TableDto[] getTableByGameType(String gameType) {
        Tables tables[] = tablesRepository.findByGameType(gameType);
        return Arrays.stream(tables)
                .map(table -> new TableDto(
                        table.getTableId(),
                        table.getTableName(),
                        table.getDescription(),
                        table.getGameType(),
                        table.getMinBuyIn(),
                        table.getMaxBuyIn(),
                        table.getMaxPlayers(),
                        table.getPlayers().size(),
                        table.getIsActive(),
                        table.getCreatedAt().toString()
                ))
                .toArray(TableDto[]::new);
    }
    public TableDto getTableById(Long tableId) {
        Tables table = tablesRepository.findByTableId(tableId);
        return new TableDto(
                table.getTableId(),
                table.getTableName(),
                table.getDescription(),
                table.getGameType(),
                table.getMinBuyIn(),
                table.getMaxBuyIn(),
                table.getMaxPlayers(),
                table.getPlayers().size(),
                table.getIsActive(),
                table.getCreatedAt().toString()
        );
    }
}
