package shared.model.cards;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import shared.definitions.ResourceType;

/**
 * Represents a collection of {@link ResourceCard}
 * @author Raul Lopez
 *
 */
public class ResourceCardDeck {
	private ArrayList<ResourceCard> cards;

	/**
	 * Constructor for the deck.
	 */
	public ResourceCardDeck(ArrayList<ResourceCard> initResourceCards) {
		cards = initResourceCards;
	}
	
	/**
	 * Iterates through the deck to see if a card with the available type is available
	 * @param type The desired ResourceCard type
	 * @return true if available, false if not
	 */
	public boolean checkIfExists(ResourceType type){
		for(ResourceCard card : cards){
			if(card.getType()==type){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the count of cards in the deck of the given type
	 * @param type the Type of Resource in question
	 * @return the count of ResourceCards of that type in the Deck
	 */
	public int getCountByType(ResourceType type){
		int output = 0;
		for(ResourceCard card : cards){
			if(card.getType()==type){
				++output;
			}
		}
		return output;
	}
	
	/**
	 * Gets a read-only list of the ResourceCards
	 * @return an unmodifiable ArrayList with all ResourceCards in the deck
	 */
	public List<ResourceCard> getAllResourceCards(){
		return Collections.unmodifiableList(cards);
	}
	
	/**
	 * Add Resource card to the deck
	 * @param card The {@link ResourceCard} to be added
	 */
	public void addResourceCard(ResourceCard card) {
		this.cards.add(card);
	}
	
	/**
	 * Add the array of {@link ResourceType} to the deck. This will make new {@link ResourceCard} objects.
	 * @param resources
	 */
	public void addResources(ArrayList<ResourceType> resources) {
		for (ResourceType resourceType : resources) {
			this.cards.add(new ResourceCard(resourceType));
		}
	}

}
