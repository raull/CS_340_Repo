package shared.proxy.moves;
import shared.locations.*;

/**
 * Moves the robber, selecting the new position and player to rob
 * @author Kent
 *
 */
public class RobPlayer {

	/**
	 * The player who is robbing
	 */
	private int PlayerIndex;
	/**
	 * The player being robbed
	 */
	private int VictimIndex;
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
	public RobPlayer(int playerIndex, int victimIndex, HexLocation location) {
		super();
		PlayerIndex = playerIndex;
		VictimIndex = victimIndex;
		this.location = location;
	}

	public int getPlayerIndex() {
		return PlayerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		//INPUT VERIFICATION
		PlayerIndex = playerIndex;
	}

	public int getVictimIndex() {
		return VictimIndex;
	}

	public void setVictimIndex(int victimIndex) {
		//INPUT VERIFICATION
		VictimIndex = victimIndex;
	}

	public HexLocation getLocation() {
		return location;
	}

	public void setLocation(HexLocation location) {
		this.location = location;
	}
	
	
}
