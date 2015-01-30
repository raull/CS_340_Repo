package shared.model.game;

import shared.model.cards.ResourceCardDeck;

/**
 * This class represents a trade offer
 * @author Raul Lopez
 *
 */
public class TradeOffer {

	/**
	 * The offer rate in which the resources will be traded
	 */
	private int rate;
	/**
	 * The {@link ResourceCardDeck} with the sending resource cards
	 */
	private ResourceCardDeck buyDeck;
	/**
	 * The {@link ResourceCardDeck} with the receiving resource cards
	 */
	private ResourceCardDeck sellDeck;
	
	/**
	 * Create a new TradeOfferClass
	 * @param offerRate The rate in which the offer will be performed
	 * @param sellDeck The resource deck that will be received
	 * @param buyDeck The resource deck that will be given
	 */
	public TradeOffer(int offerRate, ResourceCardDeck sellDeck, ResourceCardDeck buyDeck) {
		this.rate = offerRate;
		this.sellDeck = sellDeck;
		this.buyDeck = buyDeck;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public ResourceCardDeck getSellDeck() {
		return sellDeck;
	}

	public void setSelleck(ResourceCardDeck sellDeck) {
		this.sellDeck = sellDeck;
	}

	public ResourceCardDeck getBuyDeck() {
		return buyDeck;
	}

	public void setBuyDeck(ResourceCardDeck sendingDeck) {
		this.buyDeck = sendingDeck;
	}
	
}
