package shared.model.cards;

import java.util.ArrayList;

import shared.definitions.PieceType;
import shared.model.game.User;

/**
 * Manages the administration of game resources (cards, buildings, etc.)
 * @author thyer
 *
 */
public class Bank {
	
	/**
	 * The collection of {@link DevCard} that the <code>Bank</code> contains
	 */
	private DevCardDeck devCardDeck;
	/**
	 * The collection of {@link ResourceCard} that the <code>Bank</code> contains
	 */
	private ResourceCardDeck resourceCardDeck;
	
	public Bank(ArrayList<DevCard> initDevCards, ArrayList<ResourceCard> initResourceCards){
		devCardDeck = new DevCardDeck(initDevCards);
		resourceCardDeck = new ResourceCardDeck(initResourceCards);
	}
	
	/**
	 * Removes the necessary ResourceCards from the player's hand and returns a randomly drawn DevCard
	 * Throws an exception if canBuyDevCard returns false
	 * @param purchaser the Player buying the DevCard
	 * @return a randomly drawn DevCard pulled from the deck
	 */
	public DevCard buyDevCard(User purchaser){
		return null;
		
	}
	
	/**
	 * Checks to see if a purchase of a DevCard
	 * @return true if possible, false if impossible
	 */
	public boolean canBuyDevCard(){
		return (devCardDeck.getCardRemaining()>0);
	}
	
	
	/**
	 * Checks to see if the desiredResourceCard can be drawn
	 * @param type a ResourceCard with the enumeration attribute of the desired card to be returned
	 * @return true if possible, false if impossible
	 */
	public boolean canDrawResourceCard(ResourceCard type){
		return resourceCardDeck.checkIfExists(type.getType());
	}
	
	/**
	 * Determines whether the given Piece can be purchased
	 * @param type the type of Piece desired
	 * @return false if the Piece cannot otherwise be awarded, else true
	 */
	public boolean canPieceBeAwarded(PieceType type){
		return true; //assuming unlimited supply of Pieces to award
	}

	public ResourceCardDeck getResourceDeck() {
		return resourceCardDeck;
	}

	public DevCardDeck getDevCardDeck() {
		return devCardDeck;
	}

	public void setDevCardDeck(DevCardDeck devCardDeck) {
		this.devCardDeck = devCardDeck;
	}

	public void setResourceCardDeck(ResourceCardDeck resourceCardDeck) {
		this.resourceCardDeck = resourceCardDeck;
	}

}
