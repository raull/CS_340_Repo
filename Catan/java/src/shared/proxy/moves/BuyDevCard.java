package shared.proxy.moves;

/**
 * Used to buy a development card
 * @author Kent
 *
 */
public class BuyDevCard {

	/**
	 * Index (0-3) of player who is buying dev. card
	 */
	private int playerIndex;

	/**
	 * Constructor to instantiate the BuyDevCard object
	 * @param playerIndex
	 */
	public BuyDevCard(int playerIndex) {
		super();
		this.playerIndex = playerIndex;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	/**
	 * If index is 0-3, sets the number
	 * @param playerIndex
	 */
	public void setPlayerIndex(int playerIndex) {
		//INPUT VERIFICATION
		this.playerIndex = playerIndex;
	}
	
	
}
