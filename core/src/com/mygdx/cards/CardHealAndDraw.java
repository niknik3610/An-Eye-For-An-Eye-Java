package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardHealAndDraw extends Card{

    public CardHealAndDraw() {
        super(0);
        
        this.title = "reset";
        this.mainBody = "Heal fully and draw 2 Cards Of The Fates";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player) {
        player.fullyHeal();
        player.drawCardsAgain(2);
    }
}