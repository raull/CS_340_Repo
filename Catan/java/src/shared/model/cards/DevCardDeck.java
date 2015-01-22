package shared.model.cards;

import java.util.ArrayList;
import java.util.Collections;

import shared.definitions.DevCardType;
import shared.model.exception.InvalidMoveException;

/**
 * A collection of Development cards
 * @author Raul Lopez
 *
 */
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
	 * 
	 * @return the remaining size of the cards ArrayList
	 */
	public int getCardRemaining(){
		return cards.size();
	}
	
	/**
	 * Removes the top card from the Deck and returns it
	 * @return the 0th element in the Deck
	 */
	public DevCard drawCard(){
		DevCard result = cards.get(0);
		removeTopCard();
		return result;
	}
	
	/**
	 * Randomizes the order of cards in the DevCardDeck
	 */
	public void shuffle(){
		
	}
	
	/**
	 * removes the 0th element from the Deck
	 */
	public void removeTopCard(){
		
	}
	
	/**
	 * Returns a read-only list of all the DevCards
	 * @return an unmodifiable List of DevCards
	 */
	public ArrayList<DevCard> getAllCards(){
		return (ArrayList<DevCard>) Collections.unmodifiableList(cards);
	}
	
	/**
	 * Gets the count of cards in the deck of the given type
	 * @param type the Type of DevCard in question
	 * @return the count of DevCards of that type in the Deck
	 */
	public int getCountByType(DevCardType type){
		return 0;
	}
	
	/**
	 * Removes a DevCard of the given DevCardType from the deck
	 * @param type the DevCardType to be removed
	 * @throws InvalidMoveException if no DevCard of the type exists in the Deck
	 */
	public void removeCardByType(DevCardType type) throws InvalidMoveException{
		
	}

}
