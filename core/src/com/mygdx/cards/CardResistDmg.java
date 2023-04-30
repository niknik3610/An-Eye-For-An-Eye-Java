package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardResistDmg extends Card{
    private int resistPercent;

    public CardResistDmg() {
        super(0);

        resistPercent = 40;
        this.title = "mental strength";
        this.mainBody = "Take " + resistPercent + "% less damage from enemies";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player) {
        player.updDmgTaken(-resistPercent);;
    }
}