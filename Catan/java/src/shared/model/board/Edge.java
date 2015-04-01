package shared.model.board;

import java.util.ArrayList;

import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.model.board.piece.Road;
import shared.model.game.User;

/** This class provides the representation of an Edge on a game Map
 * 
 * @author Raul Lopez
 *
 */
public class Edge {
	
	/**
	 * Location of the edge described with {@link EdgeLocation}
	 */
	private EdgeLocation location;
	/**
	 * Direction of the edge described with {@link EdgeDirection}
	 */
	private EdgeDirection direction;
	/**
	 * The list of {@link HexTile} that the edge relates to
	 */
	private ArrayList<HexTile> hexTiles;
	/**
	 * The {@link Road} that the edge has on it, is initially null
	 */
	private Road road;
	
	//Constructors
	
	/**
	 * Constructor to instantiate an <code>Edge</code> with direction, location and the hexTiles
	 * @param location
	 * @param direction
	 * @param hexTiles
	 */
	public Edge(EdgeLocation location) {
 		this.direction = direction;
 		this.location = location;
 		this.hexTiles = hexTiles;

		road = null;
	}
	

	//Getters
	public EdgeLocation getLocation() {
		return location;
	}
	public EdgeDirection getDirection() {
		return direction;
	}
	public ArrayList<HexTile> getHexTiles() {
		return hexTiles;
	}
	public Road getRoad(){
		return road;
	}
	/**
	 * Determines whether the edge is currently occupied by a Road
	 * @return true if the Edge's road object is not null, false otherwise
	 */
	public boolean isOccupiedByRoad(){
		return (road!=null);
	}
	
	/**
	 * Determines whether the User has an adjoining Piece on this edge
	 * @param user the given User
	 * @return true if a building or road is adjoining to the current edge, false otherwise
	 */
	public boolean hasAdjoiningPiece(User user){
		return false; //need to think more about this
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direction == null) ? 0 : direction.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (direction != other.direction)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		return true;
	}
	
}
