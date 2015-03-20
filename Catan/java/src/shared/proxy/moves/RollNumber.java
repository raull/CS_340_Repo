package shared.proxy.moves;

/**
 * Class used to inform the server that a player has and what a
 * player has rolled
 * @author Kent
 *
 */
public class RollNumber {

	private String type;
	/**
	 * The Index of the player (0-3)
	 */
	private int playerIndex;
	/**
	 * The number rolled (2-12)
	 */
	private int number;
	
	/**
	 * Constructor to instantiate the RollNumber object
	 * @param playerId
	 * @param number
	 */
	public RollNumber(int PlayerIndex, int number) {
		super();
		playerIndex = PlayerIndex;
		this.number = number;
		type = "rollNumber";
	}

	public int getplayerIndex() {
		return playerIndex;
	}

	/**
	 * If player index is 0-3 and is not used by another player, sets the 
	 * index
	 * @param playerIndex
	 */
	public void setPlayerIndex(int playerIndex) {
		//INPUT VERIFICATION
		playerIndex = playerIndex;
	}

	public int getNumber() {
		return number;
	}

	/**
	 * Checks to ensure that the number is 2-12, and sets it.
	 * @param number
	 */
	public void setNumber(int number) {
		//INPUT VERIFICATION
		this.number = number;
	}
	
	
}
