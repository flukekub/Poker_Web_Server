package com.pkpj.temp.manager;

import com.pkpj.temp.model.Hand;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class HandManager {
    private final Map<Long, Hand> hands = new ConcurrentHashMap<>();

    public Hand get(Long tableId) {
        return hands.get(tableId);
    }

    public void put(Long tableId, Hand hand) {
        hands.put(tableId, hand);
    }

    public void remove(Long tableId) {
        hands.remove(tableId);
    }

    public boolean exists(Long tableId) {
        return hands.containsKey(tableId);
    }
}
