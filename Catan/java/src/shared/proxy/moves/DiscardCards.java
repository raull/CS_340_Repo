package shared.proxy.moves;


/**
 * Used to discard specified resource cards
 * @author Kent
 *
 */
public class DiscardCards {

	private String type;
	/**
	 * Who is discarding cards
	 */
	private int playerIndex;
	/**
	 * The cards being discarded
	 */
	private ResourceList discardedCards;
	
	/**
	 * Constructor for the DiscardCards object
	 * @param playerIndex
	 * @param discardedCards
	 */
	public DiscardCards(int playerIndex, ResourceList discardedCards) {
		super();
		this.playerIndex = playerIndex;
		this.discardedCards = discardedCards;
		type = "discardCards";
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public ResourceList getDiscardedCards() {
		return discardedCards;
	}

	public void setDiscardedCards(ResourceList discardedCards) {
		this.discardedCards = discardedCards;
	}
}
