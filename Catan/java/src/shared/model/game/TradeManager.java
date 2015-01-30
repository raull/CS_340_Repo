package shared.model.game;

import shared.definitions.ResourceType;
import shared.model.board.Port;
import shared.model.cards.ResourceCardDeck;
import shared.model.exception.InvalidMoveException;

/**
 * This class manages the trades between user-user and user-port
 * @author Raul Lopez Villalpando
 *
 */
public class TradeManager {
	
	/**
	 * Returns either if the offer can be made (if it's valid)
	 * @param buyer The user making the offer
	 * @param seller The user receiving the offer
	 * @param offer The offer that the first user is making
	 * @return Whether or not the offer is valid
	 */
	public static boolean canMakeOffer(User buyer, User seller, TradeOffer offer) {
		
		//First, check that buyer has the resources
		ResourceCardDeck buyOfferDeck = offer.getBuyDeck();
		ResourceCardDeck sendingDeck = buyer.getHand().getResourceCards();
		
		if (!hasEnoughResources(sendingDeck, buyOfferDeck)) {
			return false;
		}
		
		//Second, check that seller has the resources
		ResourceCardDeck sellOfferDeck = offer.getSellDeck();
		ResourceCardDeck receiveDeck = seller.getHand().getResourceCards();		
		
		if (!hasEnoughResources(receiveDeck, sellOfferDeck)) {
			return false;
		}
		
		return true;
	}
	
	//Helper Methods
	
	/**
	 * Check if the Deck trying to buy has enough resources that the offer Deck specifies
	 * @param buyDeck
	 * @param offerDeck
	 * @return true if the buyDEck has enough resources, false if it doesn't
	 */
	public static boolean hasEnoughResources(ResourceCardDeck buyDeck, ResourceCardDeck offerDeck) {
		//Check that deck has enough the resources
		for (ResourceType resourceType : ResourceType.values()) {
			int offerResourceCount = offerDeck.getCountByType(resourceType);
			int sendingResourceCount = buyDeck.getCountByType(resourceType);
			
			if (sendingResourceCount < offerResourceCount) {
				return false;
			}
		}
		
		return true;
	}
}
