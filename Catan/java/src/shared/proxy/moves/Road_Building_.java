package shared.proxy.moves;

import shared.locations.*;
/**
 * Used to play a "Road Building" card from your hand
 * @author Kent
 *
 */
public class Road_Building_ {

	private String type;
	/**
	 * Index of Player who is building roads
	 */
	private int playerIndex;
	/**
	 * First location to build road
	 */
	private EdgeLocation location1;
	/**
	 * Second location to build road
	 */
	private EdgeLocation location2;
	
	/**
	 * Constructor to instantiate Road_Building_ object
	 * @param playerIndex
	 * @param location1
	 * @param location2
	 */
	public Road_Building_(int playerIndex, EdgeLocation location1,
			EdgeLocation location2) {
		super();
		this.playerIndex = playerIndex;
		this.location1 = location1;
		this.location2 = location2;
		type = "Road_Building";
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		// INPUT VALIDATION
		this.playerIndex = playerIndex;
	}

	public EdgeLocation getLocation1() {
		return location1;
	}

	public void setLocation1(EdgeLocation location1) {
		this.location1 = location1;
	}

	public EdgeLocation getLocation2() {
		return location2;
	}

	public void setLocation2(EdgeLocation location2) {
		this.location2 = location2;
	}
	
	
}
