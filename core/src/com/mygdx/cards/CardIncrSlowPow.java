package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardIncrSlowPow extends Card{

    private int percentSlowIncr;

    public CardIncrSlowPow() {
        super(0);
        
        percentSlowIncr = 75;
        this.title = "charming presence";
        this.mainBody = "All of your slow effects are " + percentSlowIncr + "% stronger";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player) {
        player.updSlowPercent(percentSlowIncr);
    }
    
}