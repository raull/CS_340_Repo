package shared.model.game;

import shared.definitions.PieceType;
import shared.definitions.ResourceType;
import shared.model.cards.ResourceCard;
import shared.model.cards.ResourceCardDeck;

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
		ResourceCardDeck sendOfferDeck = offer.getSendingDeck();
		ResourceCardDeck senderDeck = buyer.getHand().getResourceCards();
		
		if (!hasEnoughResources(senderDeck, sendOfferDeck)) {
			return false;
		}
		
		//Second, check that seller has the resources
		ResourceCardDeck receiveOfferDeck = offer.getReceivingDeck();
		ResourceCardDeck receiverDeck = seller.getHand().getResourceCards();		
		
		if (!hasEnoughResources(receiverDeck, receiveOfferDeck)) {
			return false;
		}
		
		return true;
	}
	
	//Helper Methods
	
	/**
	 * Check if the Deck trying to buy has enough resources that the offer Deck specifies
	 * @param buyerDeck The Deck to check if it has enough resources
	 * @param compareDeck The Deck with the resources to compare
	 * @return true if the buyerDeck has enough resources specified by compareDeck, false if it doesn't
	 */
	public static boolean hasEnoughResources(ResourceCardDeck buyerDeck, ResourceCardDeck compareDeck) {
		//Check that deck has enough the resources
		for (ResourceType resourceType : ResourceType.values()) {
			int offerResourceCount = compareDeck.getCountByType(resourceType);
			int sendingResourceCount = buyerDeck.getCountByType(resourceType);
			
			if (sendingResourceCount < offerResourceCount) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Returns the price for the specified {@link PieceType}.
	 * @param type The piece type to get the price
	 * @return Returns a deck specifying the price. Returns null for <code>ROBBER</code> type
	 */
	public static ResourceCardDeck priceForPiece(PieceType type) {
		
		ResourceCardDeck priceDeck = new ResourceCardDeck();
		
		switch (type) {
		case ROAD:
			priceDeck.addResourceCard(new ResourceCard(ResourceType.BRICK));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.WOOD));
			break;
		case CITY:
			priceDeck.addResourceCard(new ResourceCard(ResourceType.ORE));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.ORE));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.ORE));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.WHEAT));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.WHEAT));
			break;
		case SETTLEMENT:
			priceDeck.addResourceCard(new ResourceCard(ResourceType.BRICK));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.WOOD));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.SHEEP));
			priceDeck.addResourceCard(new ResourceCard(ResourceType.WHEAT));
			break;
		default:
			return null;
		}
		
		return priceDeck;
	}
	
	public static ResourceCardDeck priceForDevCard() {
		
		ResourceCardDeck priceDeck = new ResourceCardDeck();
		priceDeck.addResourceCard(new ResourceCard(ResourceType.ORE));
		priceDeck.addResourceCard(new ResourceCard(ResourceType.WHEAT));
		priceDeck.addResourceCard(new ResourceCard(ResourceType.SHEEP));
		
		return priceDeck;
	}
}
