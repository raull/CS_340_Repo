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
	 * The {@link ResourceCardDeck} with the receiving resource cards
	 */
	private ResourceCardDeck receiveDeck;
	/**
	 * The {@link ResourceCardDeck} with the sending resource cards
	 */
	private ResourceCardDeck sendDeck;
	
	/**
	 * Create a new TradeOfferClass
	 * @param offerRate The rate in which the offer will be performed
	 * @param receiveDeck The resource deck that will be received
	 * @param sendDeck The resource deck that will be given
	 */
	public TradeOffer(int offerRate, ResourceCardDeck receiveDeck, ResourceCardDeck sendDeck) {
		this.rate = offerRate;
		this.receiveDeck = receiveDeck;
		this.sendDeck = sendDeck;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public ResourceCardDeck getReceiveDeck() {
		return receiveDeck;
	}

	public void setReceiveDeck(ResourceCardDeck receivingDeck) {
		this.receiveDeck = receivingDeck;
	}

	public ResourceCardDeck getSendDeck() {
		return sendDeck;
	}

	public void setSendDeck(ResourceCardDeck sendingDeck) {
		this.sendDeck = sendingDeck;
	}
	
}
