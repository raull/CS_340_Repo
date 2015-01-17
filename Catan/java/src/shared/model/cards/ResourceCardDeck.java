package shared.model.cards;

import java.util.ArrayList;

import shared.definitions.ResourceType;

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
	 * @return
	 */
	public ResourceCard getCard (ResourceType type){
		if(!checkIfExists(type)){
			//throw exception
		}
		return null;
	}

}
