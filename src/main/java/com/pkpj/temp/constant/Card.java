package com.pkpj.temp.constant;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public  class  Card {
    Suit suit ;
    Rank rank ;
    public String toString() {
        return rank + " of " + suit;
    }
}
