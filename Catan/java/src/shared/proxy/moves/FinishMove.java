package shared.proxy.moves;

/**
 * Lets the server know that a player's turn has ended.
 * @author Kent
 *
 */
public class FinishMove {

	private String type;
	/**
	 * Index (0-3) of the player whose turn has ended.
	 */
	private Integer playerIndex;

	/**
	 * Constructor to instantiate the FinishMove object
	 * @param playerIndex
	 */
	public FinishMove(Integer playerIndex) {
		super();
		this.playerIndex = playerIndex;
		type = "finishTurn";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		//INPUT VERIFICATION
		this.playerIndex = playerIndex;
	}
	
	
}
