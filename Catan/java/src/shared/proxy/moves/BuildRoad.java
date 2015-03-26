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
	private Integer playerIndex;
	/**
	 * Where the road is to be built
	 */
	private comEdgeLoc roadLocation;
	/**
	 * Whether the location is free (doesn't cost resources, used for setup).
	 */
	private Boolean free;
	
	/**
	 * Constructor to instantiate the BuildRoad object
	 * @param playerIndex
	 * @param roadLocation
	 * @param free
	 */
	public BuildRoad(Integer playerIndex, EdgeLocation roadLocation, Boolean free) {
		super();
		this.playerIndex = playerIndex;
		this.roadLocation = new comEdgeLoc(roadLocation);
		this.free = free;
		type = "buildRoad";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		this.playerIndex = playerIndex;
	}

	public comEdgeLoc getRoadLocation() {
		return roadLocation;
	}

	public void setRoadLocation(EdgeLocation roadLocation) {
		this.roadLocation = new comEdgeLoc(roadLocation);
	}

	public Boolean isFree() {
		return free;
	}

	public void setFree(Boolean free) {
		this.free = free;
	}
	
}
