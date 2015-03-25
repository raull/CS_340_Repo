package shared.model.cards;

import java.util.ArrayList;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;

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
	private ResourceCardDeck resourceCardDeck = new ResourceCardDeck();
	
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
	 * if a dev card could be removed
	 * @param type
	 * @return
	 */
	public boolean canRemoveDevCard(DevCardType type) {
		return devCardDeck.getAllCards().contains(new DevCard(type));
	}
	
	/**
	 * if a resource card could be removed
	 * @param type
	 * @return
	 */
	public boolean canRemoveResourceCard(ResourceType type) {
		return resourceCardDeck.getAllResourceCards().contains(new ResourceCard(type));
	}
	
	/**
	 * moves the new dev cards to usable deck
	 */
	public void moveNewToUsable() {
		//add each new dev card into devcard deck
		for(DevCard devCard : this.newDevCardDeck.getAllCards()) {
			this.devCardDeck.addDevCard(devCard);
		}
		//empty out new dev cards
		this.newDevCardDeck = new DevCardDeck(new ArrayList<DevCard>());
	}
}
