package shared.proxy.communication;
/**
 * Used to offer a domestic trade to another player
 * @author Kent
 *
 */
public class OfferTrade {

	/**
	 * Who is offering the trade
	 */
	private int playerIndex;
	/**
	 * The proposed offer (what the player gives and gets)
	 */
	private ResourceList offer;
	/**
	 * The player to whom the offer is being made
	 */
	private int receiver;
	
	/**
	 * Constructor to instantiate the OfferTrade object
	 * @param playerIndex
	 * @param offer
	 * @param receiver
	 */
	public OfferTrade(int playerIndex, ResourceList offer, int receiver) {
		super();
		this.playerIndex = playerIndex;
		this.offer = offer;
		this.receiver = receiver;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public ResourceList getOffer() {
		return offer;
	}

	public void setOffer(ResourceList offer) {
		this.offer = offer;
	}

	public int getReceiver() {
		return receiver;
	}

	public void setReceiver(int receiver) {
		this.receiver = receiver;
	}
}
