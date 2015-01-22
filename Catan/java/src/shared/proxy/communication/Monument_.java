package shared.proxy.communication;

/**
 * Used to play a "Monument" card from your hand to get a victory point.
 * @author Kent
 *
 */
public class Monument_ {

	/**
	 * Who's playing this dev. card.
	 */
	private int playerIndex;

	/**
	 * Constructor to instantiate the Monument_ object
	 * @param playerIndex
	 */
	public Monument_(int playerIndex) {
		super();
		this.playerIndex = playerIndex;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}
	
}
