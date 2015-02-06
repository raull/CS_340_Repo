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
	private ArrayList<DevCard> cards = new ArrayList<DevCard>();
	
	/**
	 * Constructor for the deck
	 */
	public DevCardDeck(ArrayList<DevCard> initDevCards){
		cards = initDevCards;
	}
	
	public DevCardDeck() {
		
	}
	
	/**
	 * 
	 * @return the remaining size of the cards ArrayList
	 */
	public int getCardRemaining(){
		return cards.size();
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
		int output = 0;
		for (DevCard card : cards){
			if(card.getType() ==type){
				++output;
			}
		}
		return output;
	}

}
