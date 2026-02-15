package com.pkpj.temp.services;

import com.pkpj.temp.repositories.HandRepository;
import com.pkpj.temp.repositories.PlayerActionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class HandService {
    private final HandRepository handRepository;
    private final PlayerActionRepository playerActionRepository;

    // Convert Hand POJO to HandEntity for persistence
//    public HandEntity saveHand(Hand hand) {
//
//        GameTable gameTable = gameTableRepository.findById(hand.getGameTableId())
//                .orElseThrow(() -> new IllegalArgumentException("Table not found"));
//
//        HandEntity entity = new HandEntity();
//        entity.setGameTable(gameTable);
//        entity.setPlayers(hand.getPlayers().stream()
//                .map(player -> String.valueOf(player.getUserId()))
//                .collect(Collectors.joining(",")));
//        entity.setCommunityCards(hand.getCommunityCards().stream()
//                .map(card -> card.getSuit().name() + card.getRank().name())
//                .reduce((a, b) -> a + "," + b)
//                .orElse(""));
//        entity.setPotSize(hand.getPotSize());
//        entity.setStatus(hand.getStatus());
//        entity.setBettingRound(hand.getBettingRound());
//        entity.setCurrentActorIndex(hand.getCurrentActorIndex());
//        entity.setCreatedAt(java.time.LocalDateTime.now());
//
//        HandEntity saved = handRepository.save(entity);
//        hand.setHandId(saved.getHandId());
//        return saved;
//    }



    // Save player action
//    public PlayerActionDto savePlayerAction(Long handId, PlayerAction action) {
//        HandEntity hand = handRepository.findById(handId)
//                .orElseThrow(() -> new IllegalArgumentException("Hand not found"));
//
//        int nextSequence = (int) (playerActionRepository.countByHandHandId(handId) + 1);
//
//        PlayerActionEntity playerActionEntity = new PlayerActionEntity();
//        playerActionEntity.setHand(hand);
//        playerActionEntity.setPlayerId(action.getPlayerId());
//        playerActionEntity.setActionType(action.getActionType());
//        playerActionEntity.setAmount(action.getAmount());
//        playerActionEntity.setBettingRound(action.getBettingRound());
//        playerActionEntity.setSequenceNumber(nextSequence);
//        playerActionEntity.setTimestamp(java.time.LocalDateTime.now());
//
//        playerActionRepository.save(playerActionEntity);
//
//        return new PlayerActionDto(
//                playerActionEntity.getHand().getHandId(),
//                playerActionEntity.getPlayerId(),
//                playerActionEntity.getActionType(),
//                playerActionEntity.getAmount().toString(),
//                playerActionEntity.getBettingRound().name(),
//                playerActionEntity.getSequenceNumber()
//        );
//    }
}
