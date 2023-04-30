package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardIncrAS extends Card{

    private int incrPercent;

    public CardIncrAS() {
        super(0);
        
        incrPercent = 50;
        this.title = "impatience";
        this.mainBody = "Increase your attack speed by  " + incrPercent + "%";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player) {
        player.incrWeaponAS(incrPercent);
    }
    
}