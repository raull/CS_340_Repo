package shared.model.board;

import java.util.ArrayList;

import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

/**
 * Class that represents an intersection between more than one {@link HexTile}
 * @author Raul Lopez
 *
 */
public class Vertex {
	
	/**
	 * A {@link VertexLocation} representing the location of the <code>Vertex</code>
	 */
	private VertexLocation location;
	/**
	 * A {@link VertexDirection} representing the direction of the <code>Vertex</code>
	 */
	private VertexDirection direction;
	/**
	 * A list of {@link HexTiles} representing the hexTiles surrounding the <code>Vertex</code>
	 */
	private ArrayList<HexTile> hexTiles;
	/**
	 * The port that the <code>Vertex</code> owns
	 */
	private Port port;
	
	//Constructor

	/**
	 * Constructor for a Vertex with direction, location and surrounding hexTiles
	 * @param location A {@link VertexLocation} representing the location of the <code>Vertex</code>
	 * @param direction A {@link VertexDirection} representing the direction of the <code>Vertex</code>
	 * @param hexTiles A list of {@link HexTiles} representing the hexTiles surrounding the <code>Vertex</code>
	 */
	public Vertex(VertexLocation location, VertexDirection direction, final ArrayList<HexTile> hexTiles) {
		this.location = location;
		this.direction = direction;
		
	}
	
	//Getters
	public VertexLocation getLocation() {
		return location;
	}
	public VertexDirection getDirection() {
		return direction;
	}
	public ArrayList<HexTile> getHexTiles() {
		return hexTiles;
	}
	public Port getPort() {
		return port;
	}

	
}
