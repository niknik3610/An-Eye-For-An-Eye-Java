package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardIncrSpeedOK extends Card{

    private int incrSpeedPercent;
    private int boostDur; // seconds

    public CardIncrSpeedOK() {
        super(35);

        incrSpeedPercent = 20;
        boostDur = 10;
        this.title = "essence high";
        this.mainBody = "Upon realeasing a soul, increase your speed by " + incrSpeedPercent + "% for " + boostDur + " seconds. This can stack";
        this.type = CONSTANTS.ON_KILL;
        this.timer = boostDur;
    }

    @Override
    public void applyCard(Player player) {
        player.playerSpeedUp(1 + incrSpeedPercent/100f);
    } 
}
