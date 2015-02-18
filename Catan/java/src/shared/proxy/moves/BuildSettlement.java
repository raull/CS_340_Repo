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
	private int playerIndex;
	/**
	 * Where the settlement is to be built
	 */
	private VertexLocation vertexLocation;
	/**
	 * Whether this is placed for free (used for setup)
	 */
	private boolean free;
	
	/**
	 * Constructor to instantiate the BuildSettlement object
	 * @param playerIndex
	 * @param vertexLocation
	 * @param free
	 */
	public BuildSettlement(int playerIndex, VertexLocation vertexLocation,
			boolean free) {
		super();
		this.playerIndex = playerIndex;
		this.vertexLocation = vertexLocation;
		this.free = free;
		type = "buildSettlement";
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

	public boolean isFree() {
		return free;
	}

	public void setFree(boolean free) {
		this.free = free;
	}
}
