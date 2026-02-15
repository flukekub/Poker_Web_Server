package com.pkpj.temp.services;

import com.pkpj.temp.constant.*;
import com.pkpj.temp.dtos.*;
import com.pkpj.temp.dtos.games.GameTableDto;
import com.pkpj.temp.dtos.games.HandDto;
import com.pkpj.temp.dtos.games.TablePlayerDto;
import com.pkpj.temp.entities.*;
import com.pkpj.temp.manager.HandManager;
import com.pkpj.temp.model.Hand;
import com.pkpj.temp.model.Player;
import com.pkpj.temp.repositories.TablePlayerRepository;
import com.pkpj.temp.repositories.TablesRepository;
import com.pkpj.temp.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameService {
    private final TablesRepository tablesRepository;
    private final HandManager handManager;

    @Transactional
    public HandDto startGame(Long tableId, int dealerSeatNumber, BigDecimal smallBlindAmount, BigDecimal bigBlindAmount) {
        Tables table = tablesRepository.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Table not found"));

        Hand hand = new Hand(tableId);

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
