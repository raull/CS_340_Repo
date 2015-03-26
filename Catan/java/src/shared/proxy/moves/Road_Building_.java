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
	private Integer playerIndex;
	/**
	 * First location to build road
	 */
	private comEdgeLoc spot1;
	/**
	 * Second location to build road
	 */
	private comEdgeLoc spot2;
	
	/**
	 * Constructor to instantiate Road_Building_ object
	 * @param playerIndex
	 * @param location1
	 * @param location2
	 */
	public Road_Building_(Integer playerIndex, EdgeLocation location1,
			EdgeLocation location2) {
		super();
		this.playerIndex = playerIndex;
		this.spot1 = new comEdgeLoc(location1);
		this.spot2 = new comEdgeLoc(location2);
		type = "Road_Building";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		
		this.playerIndex = playerIndex;
	}

	public comEdgeLoc getLocation1() {
		return spot1;
	}

	public void setLocation1(EdgeLocation location1) {
		this.spot1 = new comEdgeLoc(location1);
	}

	public comEdgeLoc getLocation2() {
		return spot2;
	}

	public void setLocation2(EdgeLocation location2) {
		this.spot2 = new comEdgeLoc(location2);
	}
	
	
}
