package shared.proxy.moves;
import shared.locations.*;
/**
 * Used to build a city at a specified location.
 * @author Kent
 *
 */
public class BuildCity {

	private String type;
	/**
	 * Who is building the city.
	 */
	private int playerIndex;
	/**
	 * Where the city is to be built.
	 */
	private comVertexLoc vertexLocation;
	
	/**
	 * Constructor to instantiate the BiuldCity object
	 * @param playerIndex
	 * @param vertexLocation
	 */
	public BuildCity(int playerIndex, VertexLocation vertexLocation) {
		super();
		this.playerIndex = playerIndex;
		this.vertexLocation = new comVertexLoc(vertexLocation);
		type = "buildCity";
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public comVertexLoc getVertexLocation() {
		return vertexLocation;
	}

	public void setVertexLocation(VertexLocation vertexLocation) {
		this.vertexLocation = new comVertexLoc(vertexLocation);
	}
}
