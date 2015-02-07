package shared.model.game;

import shared.definitions.PortType;
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
	private ResourceCardDeck sendingDeck = new ResourceCardDeck();
	/**
	 * The {@link ResourceCardDeck} with the receiving resource cards
	 */
	private ResourceCardDeck receivingDeck = new ResourceCardDeck();
	
	/**
	 * index of the person offering the trade
	 */
	private int senderIndex;
	
	/**
	 * index of the person the trade was offered to
	 */
	private int receiverIndex;
	/**
	 * The PortType in case the trade is maritime
	 */
	private PortType portType;
	
	/**
	 * Create a new TradeOfferClass
	 * @param offerRate The rate in which the offer will be performed
	 * @param receivingDeck The resource deck that will be received
	 * @param sendingDeck The resource deck that will be given
	 */
	public TradeOffer(int offerRate, ResourceCardDeck receivingDeck, ResourceCardDeck sendingDeck) {
		this.rate = offerRate;
		this.receivingDeck = receivingDeck;
		this.sendingDeck = sendingDeck;
	}
	
	public TradeOffer(ResourceCardDeck offeredDeck, ResourceCardDeck requestedDeck) { 
		this.sendingDeck = offeredDeck; 
		this.receivingDeck = requestedDeck;
	}
	
	public TradeOffer() {
		
	}
	

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public ResourceCardDeck getReceivingDeck() {
		return receivingDeck;
	}

	public void setReceivingDeck(ResourceCardDeck receivingDeck) {
		this.receivingDeck = receivingDeck;
	}

	public ResourceCardDeck getSendingDeck() {
		return sendingDeck;
	}

	public void setSendingDeck(ResourceCardDeck sendingDeck) {
		this.sendingDeck = sendingDeck;
	}

	public PortType getPortType() {
		return portType;
	}

	public void setPortType(PortType portType) {
		this.portType = portType;
	}

	public int getSenderIndex() {
		return senderIndex;
	}

	public void setSenderIndex(int senderIndex) {
		this.senderIndex = senderIndex;
	}

	public int getReceiverIndex() {
		return receiverIndex;
	}

	public void setReceiverIndex(int receiverIndex) {
		this.receiverIndex = receiverIndex;
	}
	
	
	
}
