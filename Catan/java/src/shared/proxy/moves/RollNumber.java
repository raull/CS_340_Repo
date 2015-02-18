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
	private int PlayerIndex;
	/**
	 * The number roled (2-12)
	 */
	private int number;
	
	/**
	 * Constructor to instantiate the RollNumber object
	 * @param playerIndex
	 * @param number
	 */
	public RollNumber(int playerIndex, int number) {
		super();
		PlayerIndex = playerIndex;
		this.number = number;
		type = "rollNumber";
	}

	public int getPlayerIndex() {
		return PlayerIndex;
	}

	/**
	 * If player index is 0-3 and is not used by another player, sets the 
	 * index
	 * @param playerIndex
	 */
	public void setPlayerIndex(int playerIndex) {
		//INPUT VERIFICATION
		PlayerIndex = playerIndex;
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
