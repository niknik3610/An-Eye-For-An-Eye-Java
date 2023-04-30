package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardAddDash extends Card{


    public CardAddDash() {
        super(0);

        this.title = "agility";
        this.mainBody = "You can now dash upon tapping SPACE";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    @Override
    public void applyCard(Player player) {
        player.enableDash();
    }
    
}