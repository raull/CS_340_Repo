package shared.model;

import shared.definitions.HexType;

public class HexTile {

	private HexType type;
	
	//Constructor
	public HexTile(HexType type) {
		this.type = type;
	}
	
	
	//Getters and Setters
	public HexType getType() {
		return this.type;
	}
}
