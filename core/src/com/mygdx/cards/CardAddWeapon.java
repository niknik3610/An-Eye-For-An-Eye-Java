package com.mygdx.cards;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.objects.Player;

public class CardAddWeapon extends Card{
    
    public CardAddWeapon() {
        super(0);

        this.title = "seeing triple";
        this.mainBody = "Choose another weapon to add to your arsenal";
        this.type = CONSTANTS.ON_CHOSEN;
    }

    // TODO: add a weapon choosing screen here
    @Override
    public void applyCard(Player player) {
        player.addWeapon();
    }
    
}
