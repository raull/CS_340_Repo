package shared.model.cards;

import java.util.ArrayList;

public class DevCardDeck {
	private ArrayList<DevCard> cards;
	
	/**
	 * Constructor for the deck
	 * @param allCards whether the deck should contain all the initial DevCards or not. 
	 * This initializes the bank and player's hands properly
	 */
	public DevCardDeck(boolean allCards){
		
	}
	
	/**
	 * returns the remaining size of the cards ArrayList
	 */
	public int getCardRemaining(){
		return cards.size();
	}
	
	public DevCard drawCard(){
		DevCard result = cards.get(0);
		removeTopCard();
		return result;
	}
	
	public void shuffle(){
		
	}
	
	public void removeTopCard(){
		
	}

}
