package shared.model.board;

import shared.definitions.HexType;
import shared.locations.HexLocation;

/**
 * This class represents a Hex on the Map
 * @author Raul Lopez
 *
 */
public class HexTile {

	/**
	 * The type of the <code> HexTile </code> represented by {@link HexType}
	 */
	private HexType type;
	/**
	 * The location of the <code> HexTile </code> represented by {@link HexLocation}
	 */
	private HexLocation location;
	
	
	//Constructors
	
	/**
	 * Constructor to instantiate a new <code>HexTile</code> with a provided location and type
	 * @param type Type described by the enumeration in {@link HexType}
	 * @param location Location described by the enumeration in {@link HexLocation}
	 */
	public HexTile(HexType type, HexLocation location) {
		this.type = type;
		this.location = location;
	}
	
	
	//Getters and Setters
	public HexType getType() {
		return this.type;
	}


	public HexLocation getLocation() {
		return location;
	}

}
