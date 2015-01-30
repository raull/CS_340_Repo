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
	 * Make an offer between users
	 * @param user1 The user making the offer
	 * @param user2 The user receiving the offer
	 * @param offer The offer that the first user is making
	 * @throws InvalidMoveException
	 */
	public static void makeOffer(User user1, User user2, TradeOffer offer) throws InvalidMoveException {
		
	}
	
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
	
	/**
	 * Make an offer between port and user
	 * @param user The user making the offer
	 * @param port The port receiving the offer
	 * @param offer The offer that the first user is making
	 * @throws InvalidMoveException
	 */
	public static void makeOffer(User user, Port port, TradeOffer offer) throws InvalidMoveException {
		
	}
	
	/**
	 * Returns either if the offer can be made (if it's valid)
	 * @param user The user making the offer
	 * @param port The port receiving the offer
	 * @param offer The offer that the first user is making
	 * @return Whether or not the offer is valid
	 */
	public static boolean canMakeOffer(User user, Port port, TradeOffer offer) {
		return false;
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
