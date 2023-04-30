package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardIncrXPGain extends Card{

    private int percentXPGain;

    public CardIncrXPGain() {
        super(0);
        
        percentXPGain = 40;
        this.title = "precious insight";
        this.mainBody = "Upon releasing a soul, gain " + percentXPGain + "% more XP";
        this.type = CONSTANTS.ON_KILL;
    }

    @Override
    public void applyCard(Player player) {
        player.incrXPMult(percentXPGain);
    }
    
}