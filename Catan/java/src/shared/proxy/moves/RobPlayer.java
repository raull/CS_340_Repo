package shared.proxy.moves;
import shared.locations.*;

/**
 * Moves the robber, selecting the new position and player to rob
 * @author Kent
 *
 */
public class RobPlayer {

	private String type;
	/**
	 * The player who is robbing
	 */
	private int playerIndex;
	/**
	 * The player being robbed
	 */
	private int victimIndex;
	/**
	 * The new location of the robber
	 */
	private HexLocation location;
	
	/**
	 * Constructor to instantiate the RobPlayer object.
	 * @param playerIndex
	 * @param victimIndex
	 * @param location
	 */
	public RobPlayer(int PlayerIndex, int victimIndex, HexLocation location) {
		super();
		playerIndex = PlayerIndex;
		this.victimIndex = victimIndex;
		this.location = location;
		type = "robPlayer";
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int PlayerIndex) {
		//INPUT VERIFICATION
		playerIndex = PlayerIndex;
	}

	public int getVictimIndex() {
		return victimIndex;
	}

	public void setVictimIndex(int victimIndex) {
		//INPUT VERIFICATION
		victimIndex = victimIndex;
	}

	public HexLocation getLocation() {
		return location;
	}

	public void setLocation(HexLocation location) {
		this.location = location;
	}
	
	
}
