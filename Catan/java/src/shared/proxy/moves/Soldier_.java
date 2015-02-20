package shared.proxy.moves;

import shared.locations.*;
/**
 * Used to play a "Soldier" card from the hand
 * @author Kent
 *
 */
public class Soldier_ {

	private String type;
	/**
	 * Index of player who is using the card
	 */
	private int playerIndex;
	/**
	 * Index of the player who is being robbed
	 */
	private int victimIndex;
	/**
	 * Location to where the robber is being moved.
	 */
	private HexLocation location;
	
	/**
	 * Constructor for the Soldier_ object
	 * @param playerIndex
	 * @param victimIndex
	 * @param location
	 */
	public Soldier_(int playerIndex, int victimIndex, HexLocation location) {
		super();
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;
		this.location = location;
		type = "Soldier";
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		//INPUT VALIDATION
		this.playerIndex = playerIndex;
	}

	public int getVictimIndex() {
		return victimIndex;
	}

	public void setVictimIndex(int victimIndex) {
		//INPUT VALIDATION
		this.victimIndex = victimIndex;
	}

	public HexLocation getLocation() {
		return location;
	}

	public void setLocation(HexLocation location) {
		this.location = location;
	}
}
