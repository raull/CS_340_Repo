package shared.proxy.moves;
import shared.locations.*;
/**
 * Used to build a road at a specified location
 * @author Kent
 *
 */
public class BuildRoad {

	private String type;
	/**
	 * Who is building a road
	 */
	private int playerIndex;
	/**
	 * Where the road is to be built
	 */
	private comEdgeLoc roadLocation;
	/**
	 * Whether the location is free (doesn't cost resources, used for setup).
	 */
	private boolean free;
	
	/**
	 * Constructor to instantiate the BuildRoad object
	 * @param playerIndex
	 * @param roadLocation
	 * @param free
	 */
	public BuildRoad(int playerIndex, EdgeLocation roadLocation, boolean free) {
		super();
		this.playerIndex = playerIndex;
		this.roadLocation = new comEdgeLoc(roadLocation);
		this.free = free;
		type = "buildRoad";
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public comEdgeLoc getRoadLocation() {
		return roadLocation;
	}

	public void setRoadLocation(EdgeLocation roadLocation) {
		this.roadLocation = new comEdgeLoc(roadLocation);
	}

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
	
}
