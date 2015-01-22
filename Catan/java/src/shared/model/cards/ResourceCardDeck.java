package shared.model.cards;

import java.util.ArrayList;
import java.util.Collections;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.exception.InvalidMoveException;

/**
 * Represents a collection of {@link ResourceCard}
 * @author Raul Lopez
 *
 */
public class ResourceCardDeck {
	private ArrayList<ResourceCard> cards;

	/**
	 * Constructor for the deck. Initializes with all the cards if owned by the bank
	 * @param b whether the bank owns this Deck
	 */
	public ResourceCardDeck(boolean b) {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Iterates through the deck to see if a card with the available type is available
	 * @param type The desired ResourceCard type
	 * @return true if available, false if not
	 */
	public boolean checkIfExists(ResourceType type){
		return false;
	}
	
	/**
	 * Iterates through the deck for a card of the desired type and removes the card before returning it
	 * @param type
	 * @return the desired ResourceCard
	 * @throws InvalidMoveException if no card of the desired type is available.
	 */
	public ResourceCard getCard (ResourceType type) throws InvalidMoveException{
		if(!checkIfExists(type)){
			//throw exception
		}
		return null;
	}
	
	/**
	 * Gets the count of cards in the deck of the given type
	 * @param type the Type of Resource in question
	 * @return the count of ResourceCards of that type in the Deck
	 */
	public int getCountByType(ResourceType type){
		return 0;
	}
	
	/**
	 * Gets a read-only list of the ResourceCards
	 * @return an unmodifiable ArrayList with all ResourceCards in the deck
	 */
	public ArrayList<ResourceCard> getAllResourceCards(){
		return (ArrayList<ResourceCard>) Collections.unmodifiableCollection(cards);
	}

}
