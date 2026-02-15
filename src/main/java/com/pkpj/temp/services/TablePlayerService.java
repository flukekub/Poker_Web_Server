package com.pkpj.temp.services;

import com.pkpj.temp.constant.TableStatus;
import com.pkpj.temp.dtos.NotificationPayload;
import com.pkpj.temp.dtos.games.TablePlayerDto;
import com.pkpj.temp.entities.TablePlayer;
import com.pkpj.temp.entities.Tables;
import com.pkpj.temp.entities.User;
import com.pkpj.temp.repositories.TablePlayerRepository;
import com.pkpj.temp.repositories.TablesRepository;
import com.pkpj.temp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TablePlayerService {
    private final TablePlayerRepository tablePlayerRepository;
    private final TablesRepository tablesRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

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
                tablePlayer.getTable().getTableId(),
                tablePlayer.getStax(),
                tablePlayer.getSeatNumber(),
                tablePlayer.getJoinedAt(),
                tablePlayer.getIsSitting(),
                tablePlayer.getUser().getProfileImageUrl(),
                tablePlayer.getUser().getName()
        )).toList();
    }
    public void deleteTablePlayer(Long tablePlayerId) {
        TablePlayer tablePlayer = tablePlayerRepository.findById(tablePlayerId)
                .orElseThrow(() -> new RuntimeException("TablePlayer not found with id: " + tablePlayerId));

        Long tableId = tablePlayer.getTable().getTableId();

        tablePlayerRepository.delete(tablePlayer);

        NotificationPayload payload = new NotificationPayload(
                "TABLE" + tableId + "_LEFT",
                "Player left the table"
        );
        messagingTemplate.convertAndSend("/topic/notifications", payload);

    }
}
