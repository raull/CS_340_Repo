package shared.proxy.communication;
import shared.locations.*;
/**
 * Used to build a city at a specified location.
 * @author Kent
 *
 */
public class BuildCity {

	/**
	 * Who is building the city.
	 */
	private int playerIndex;
	/**
	 * Where the city is to be built.
	 */
	private VertexLocation vertexLocation;
	
	/**
	 * Constructor to instantiate the BiuldCity object
	 * @param playerIndex
	 * @param vertexLocation
	 */
	public BuildCity(int playerIndex, VertexLocation vertexLocation) {
		super();
		this.playerIndex = playerIndex;
		this.vertexLocation = vertexLocation;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public VertexLocation getVertexLocation() {
		return vertexLocation;
	}

	public void setVertexLocation(VertexLocation vertexLocation) {
		this.vertexLocation = vertexLocation;
	}
}
