package shared.proxy.moves;
import shared.locations.*;
/**
 * Used to build a settlement at a specified location.
 * @author Kent
 *
 */
public class BuildSettlement {

	private String type;
	/**
	 * Who is trying to build a settlement
	 */
	private Integer playerIndex;
	/**
	 * Where the settlement is to be built
	 */
	private comVertexLoc vertexLocation;
	/**
	 * Whether this is placed for free (used for setup)
	 */
	private Boolean free;
	
	/**
	 * Constructor to instantiate the BuildSettlement object
	 * @param playerIndex
	 * @param vertexLocation
	 * @param free
	 */
	public BuildSettlement(Integer playerIndex, VertexLocation vertexLocation,
			Boolean free) {
		super();
		this.playerIndex = playerIndex;
		this.vertexLocation = new comVertexLoc(vertexLocation);
		this.free = free;
		type = "buildSettlement";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		this.playerIndex = playerIndex;
	}

	public comVertexLoc getVertexLocation() {
		return vertexLocation;
	}

	public void setVertexLocation(VertexLocation vertexLocation) {
		this.vertexLocation = new comVertexLoc(vertexLocation);
	}

	public Boolean isFree() {
		return free;
	}

	public void setFree(Boolean free) {
		this.free = free;
	}
}
