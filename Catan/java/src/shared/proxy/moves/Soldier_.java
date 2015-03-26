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
	private Integer playerIndex;
	/**
	 * Index of the player who is being robbed
	 */
	private Integer victimIndex;
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
	public Soldier_(Integer playerIndex, Integer victimIndex, HexLocation location) {
		super();
		this.playerIndex = playerIndex;
		this.victimIndex = victimIndex;
		this.location = location;
		type = "Soldier";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		//INPUT VALIDATION
		this.playerIndex = playerIndex;
	}

	public Integer getVictimIndex() {
		return victimIndex;
	}

	public void setVictimIndex(Integer victimIndex) {
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
