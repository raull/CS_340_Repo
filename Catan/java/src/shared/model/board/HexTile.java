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
	
	/**
	 * The number of the hex for rolling the dice purposes
	 */
	private int number;
	/**
	 * A boolean indicating whether the Hex is currently occupied by the Robber
	 */
	private boolean robber;
	
	
	//Constructors
	
	/**
	 * Constructor to instantiate a new <code>HexTile</code> with a provided location and type
	 * @param type Type described by the enumeration in {@link HexType}
	 * @param location Location described by the enumeration in {@link HexLocation}
	 * @param number The number that represents the hex tile when rolling the dice
	 */
	public HexTile(HexType type, HexLocation location, int number) {
		this.type = type;
		this.location = location;
		this.number = number;
	}
	
	
	//Getters and Setters
	public HexType getType() {
		return this.type;
	}

	public HexLocation getLocation() {
		return location;
	}
	
	public int getNumber() {
		return number;
	}
	
	public boolean hasRobber(){
		return robber;
	}
	
	//Logic
	/**
	 * Determines whether the robber could be moved to this Hex location
	 * @return true if the robber is not already here, otherwise false
	 */
	public boolean canMoveRobberHere(){
		return !robber;
	}


}
