package shared.model.cards;

import java.util.ArrayList;

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
	
	public ArrayList<DevCard> getAllDevCards(){
		return devCardDeck.getAllCards();
	}
}
