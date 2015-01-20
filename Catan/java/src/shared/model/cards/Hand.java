package shared.model.cards;

import java.util.ArrayList;

import shared.model.exception.InvalidMoveException;
import shared.model.exception.ModelException;

/**
 * The user's hand. Contains a deck of DevCards and a deck of ResourceCards. 
 * @author thyer
 *
 */
public class Hand {
	private DevCardDeck devCardDeck;
	private ResourceCardDeck resourceCardDeck;
	
	public Hand (){
		devCardDeck = new DevCardDeck(false);
		resourceCardDeck = new ResourceCardDeck(false);
	}
	
	public ResourceCardDeck getResourceCards(){
		return resourceCardDeck;
	}
	
	public DevCardDeck getDevCards(){
		return devCardDeck;
	}
	
	/**
	 * Determines whether a card can be removed from the hand
	 * @param type the type of card to be removed
	 * @return false if the card does not exist, otherwise true
	 */
	public boolean canRemoveCard(Card type){
		return false;
	}
	
	/**
	 * Removes a card of a given type from the hand
	 * @param type the type of card being removed
	 * @throws InvalidMoveException if there are no such cards in the hand
	 * @throws ModelException if the Card type was formatted incorrectly
	 */
	public void removeCard(Card type) throws InvalidMoveException, ModelException{
		if (type.getClass() == new DevCard(null).getClass()){
			devCardDeck.removeCardByType(((DevCard) type).getType());
		}
	}
}
