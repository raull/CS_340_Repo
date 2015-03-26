package shared.proxy.moves;

/**
 * Used to buy a development card
 * @author Kent
 *
 */
public class BuyDevCard {

	private String type;
	/**
	 * Index (0-3) of player who is buying dev. card
	 */
	private Integer playerIndex;

	/**
	 * Constructor to instantiate the BuyDevCard object
	 * @param playerIndex
	 */
	public BuyDevCard(Integer playerIndex) {
		super();
		this.playerIndex = playerIndex;
		type = "buyDevCard";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	/**
	 * If index is 0-3, sets the number
	 * @param playerIndex
	 */
	public void setPlayerIndex(Integer playerIndex) {
		//INPUT VERIFICATION
		this.playerIndex = playerIndex;
	}
	
	
}
