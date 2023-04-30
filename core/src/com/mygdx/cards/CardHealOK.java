package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardHealOK extends Card{

    private int healAmnt;

    public CardHealOK() {
        super(0);
        
        healAmnt = 10;
        this.title = "breathing essence";
        this.mainBody = "Upon releasing a soul, heal by " + healAmnt;
        this.type = CONSTANTS.ON_KILL;
    }

    @Override
    public void applyCard(Player player) {
        player.playerHeal(healAmnt);
    }
    
}