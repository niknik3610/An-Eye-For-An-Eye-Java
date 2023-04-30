package com.mygdx.cards;

import java.util.ArrayList;
import java.util.Collections;

import com.mygdx.helpers.CONSTANTS;
import com.mygdx.helpers.HudHelper;
import com.mygdx.objects.Player;

public class CardHelper {
    private ArrayList<Card> drawPile;
    private HudHelper hudHelper;
    private ArrayList<Card> activeCards;
    private boolean isNextCardBoosted;
    private Player player;

    public CardHelper() {
        drawPile = instantiateCards();
        Collections.shuffle(drawPile);
        activeCards = new ArrayList<>();
        isNextCardBoosted = false;
    }

    private ArrayList<Card> instantiateCards() {
        ArrayList<Card> toReturn = new ArrayList<>();
        
        toReturn.add(new CardAddDash());           
        //toReturn.add(new CardAddWeapon()); 
        toReturn.add(new CardBoostNextCard());
        toReturn.add(new CardHealAndDraw());
        toReturn.add(new CardHealOK());
        toReturn.add(new CardIncrAS());
        toReturn.add(new CardIncrDmg());
        toReturn.add(new CardIncrMaxHP());
        toReturn.add(new CardIncrSlowPow());
        toReturn.add(new CardIncrSpeedOK());
        toReturn.add(new CardIncrXPGain());
        toReturn.add(new CardResistDmg());
        
        return toReturn;
    }

    public void drawCards(int amount) {
        if (drawPile.size() < amount) {
            return;
        }

        ArrayList<Card> drawn = new ArrayList<Card>();
        for (int i = 0; i < amount; i++) {
            drawn.add(drawPile.remove(0));
        }
        hudHelper.drawCardHud(drawn);
    }

 
    /**
     * Adds remaining cards back onto the drawPile
     * @param cards the cards that should be added
     */
    public void addCards(ArrayList<Card> cards) {
        drawPile.addAll(cards);
    }
    
    public void setHudHelper(HudHelper hudHelper) {
        this.hudHelper = hudHelper;
    }


    //used only when a player chooses a card
    public void applyCard(Card card) {
        if (card.getType() == CONSTANTS.ON_CHOSEN) {
            card.applyCard(player);
            if(isNextCardBoosted){
                isNextCardBoosted = false;
                card.applyCard(player);
            }
            return;
        }
        activeCards.add(card);
    }

    public void checkActives(int cardTypeID) {
        for (Card card : activeCards) {
            if (card.getType() == cardTypeID) {
                card.applyCard(player);
                if (card.getType() == CONSTANTS.ON_KILL && card.getTimer() > 0) {
                    player.setPlayerSpeedTimer(card.getTimer());
                }
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public ArrayList<Card> getActiveCards() {
        return activeCards;
    }

    public void boostNextCard(){
        isNextCardBoosted = true;
    }
}
