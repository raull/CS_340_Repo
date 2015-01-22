package shared.proxy.moves;

/**
 * Lets the server know that a player's turn has ended.
 * @author Kent
 *
 */
public class FinishMove {

	/**
	 * Index (0-3) of the player whose turn has ended.
	 */
	private int playerIndex;

	/**
	 * Constructor to instantiate the FinishMove object
	 * @param playerIndex
	 */
	public FinishMove(int playerIndex) {
		super();
		this.playerIndex = playerIndex;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		//INPUT VERIFICATION
		this.playerIndex = playerIndex;
	}
	
	
}
