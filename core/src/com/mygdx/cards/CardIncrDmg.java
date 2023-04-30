package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardIncrDmg extends Card{

    private int dmgIncr;

    public CardIncrDmg() {
        super(0);
        
        dmgIncr = 40;
        this.title = "steaming rage";
        this.mainBody = "Increase damage of all of your weapons by " + dmgIncr + "%";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player) {
        player.incrDmg(dmgIncr);
    }
}