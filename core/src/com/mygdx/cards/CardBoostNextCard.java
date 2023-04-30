package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardBoostNextCard extends Card{

    public CardBoostNextCard() {
        super(0);
        
        this.title = "foresight";
        this.mainBody = "Apply next card's effect twice";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player){
        player.boostNextCard();
    }
}