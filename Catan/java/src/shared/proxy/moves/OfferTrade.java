package shared.proxy.moves;


/**
 * Used to offer a domestic trade to another player
 * @author Kent
 *
 */
public class OfferTrade {

	private String type;
	/**
	 * Who is offering the trade
	 */
	private Integer playerIndex;
	/**
	 * The proposed offer (what the player gives and gets)
	 */
	private ResourceList offer;
	/**
	 * The player to whom the offer is being made
	 */
	private Integer receiver;
	
	/**
	 * Constructor to instantiate the OfferTrade object	
	 * @param playerIndex
	 * @param offer
	 * @param receiver
	 */
	public OfferTrade(Integer playerIndex, ResourceList offer, Integer receiver) {
		super();
		this.playerIndex = playerIndex;
		this.offer = offer;
		this.receiver = receiver;
		type = "offerTrade";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		this.playerIndex = playerIndex;
	}

	public ResourceList getOffer() {
		return offer;
	}

	public void setOffer(ResourceList offer) {
		this.offer = offer;
	}

	public Integer getReceiver() {
		return receiver;
	}

	public void setReceiver(Integer receiver) {
		this.receiver = receiver;
	}
}
