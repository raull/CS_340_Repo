package shared.model.cards;

import shared.definitions.PieceType;
import shared.model.board.piece.Piece;
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
	
	public Bank(){
		devCardDeck = new DevCardDeck(true);
		resourceCardDeck = new ResourceCardDeck(true);
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
	 * Checks to see if a purchase is possible for that player
	 * @param purchaser the Player buying the DevCard
	 * @return true if possible, false if impossible
	 */
	public boolean canBuyDevCard(User purchaser){
		return false;
		
	}
	
	/**
	 * Draws the top card from the DevCardDeck
	 * @return the top card from the deck
	 */
	private DevCard drawDevCard(){
		return null;
		
	}
	
	/**
	 * Checks if there are remaining ResourceCards of that type remaining, draws one from the deck if available
	 * Throws an exception if not available
	 * @param type a ResourceCard with the enumeration attribute of the desired card to be returned
	 * @return the desired ResourceCard if available
	 */
	public ResourceCard getResourceCard(ResourceCard type){
		return null;
		//throw exception if none are left
		//calls draw method on ResourceCardDeck
	}
	
	
	/**
	 * Checks to see if the desiredResourceCard can be drawn
	 * @param type a ResourceCard with the enumeration attribute of the desired card to be returned
	 * @return true if possible, false if impossible
	 */
	public boolean canDrawResourceCard(ResourceCard type){
		return false;
		
	}
	
	/**
	 * randomizes the order of cards in the DevCardDeck
	 */
	public void shuffleDevCardDeck(){
		
	}
	
	/**
	 * Awards the specified card to the specified Player
	 * @param player the Player to be awarded a card
	 * @param card the Card that the user is being given
	 */
	public void giveCardToUser(User player, Card card){
		
	}
	
	/**
	 * Awards the specified piece to the user
	 * @param player the Player to be awarded a piece
	 * @param piece the Piece that the user is being given
	 */
	public void givePieceToUser(User player, Piece piece){
		
	}
	
	/**
	 * Determines whether the given User can purchase the given Piece
	 * @param player the player attempting to purchase a piece
	 * @param type the type of Piece desired
	 * @return false if the User has insufficient resource cards or the Piece cannot otherwise be awarded
	 */
	public boolean canPieceBeAwarded(User player, PieceType type){
		return false;
	}

}
