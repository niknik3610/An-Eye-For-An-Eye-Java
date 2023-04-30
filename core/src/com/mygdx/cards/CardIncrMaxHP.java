package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardIncrMaxHP extends Card{
    
    private int healthIncr;

    public CardIncrMaxHP() {
        super(0);

        healthIncr = 50;
        this.title = "growth";
        this.mainBody = "Increase your maximum health by "+ healthIncr;
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player) {
        player.playerMaxHealthUp(healthIncr);
    }

}
