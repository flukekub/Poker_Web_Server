package com.pkpj.temp.model;

import com.pkpj.temp.constant.ActionType;
import com.pkpj.temp.constant.BettingRound;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PlayerAction {

        private Long playerId;
        private ActionType action;
        private BigDecimal amount;
        private BettingRound round;

}
