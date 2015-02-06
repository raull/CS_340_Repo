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
	
	/**
	 * The development cards deck
	 */
	private DevCardDeck devCardDeck = new DevCardDeck();
	/**
	 * The newly added development cards
	 */
	private DevCardDeck newDevCardDeck = new DevCardDeck();
	/**
	 * The resource cards deck
	 */
	private ResourceCardDeck resourceCardDeck;
	
	public Hand (DevCardDeck oldDevCards, DevCardDeck newDevCards, ResourceCardDeck resourceCards){
		devCardDeck = oldDevCards; 
		newDevCardDeck = newDevCards;
		resourceCardDeck =resourceCards;
	}
	
	public Hand() {
		
	}
	
	public ResourceCardDeck getResourceCards(){
		return resourceCardDeck;
	}
	
	public DevCardDeck getUsableDevCards(){
		return devCardDeck;
	}
	
	public DevCardDeck getNewDevCards(){
		return newDevCardDeck;
	}
	

	public void setDevCardDeck(DevCardDeck devCardDeck) {
		this.devCardDeck = devCardDeck;
	}

	public void setNewDevCardDeck(DevCardDeck newDevCardDeck) {
		this.newDevCardDeck = newDevCardDeck;
	}

	public void setResourceCardDeck(ResourceCardDeck resourceCardDeck) {
		this.resourceCardDeck = resourceCardDeck;
	}

	/**
	 * Determines whether a card can be removed from the hand
	 * @param type the type of card to be removed
	 * @return false if the card does not exist, otherwise true
	 */
	public boolean canRemoveCard(Card type){
		DevCard tempDevCard = new DevCard(null);
		ResourceCard tempResourceCard = new ResourceCard(null);
		
		/*Determines whether the card is DevCard or ResourceCard*/
		if(type.getClass()==tempDevCard.getClass()){
			return (devCardDeck.getCountByType(((DevCard)type).getType())>0); //gets count by type from usable cards
			//if a card is in newDevCards but not the playable deck, it'll return false
		}
		else if(type.getClass()==tempResourceCard.getClass()){
			return (resourceCardDeck.getCountByType(((ResourceCard)type).getType())>0); //gets count by type
		}
		else{
			return false;		//idiot-proofing, prevents incorrect inputs
		}
	}
}
