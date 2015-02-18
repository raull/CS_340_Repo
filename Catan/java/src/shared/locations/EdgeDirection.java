package shared.locations;

import com.google.gson.annotations.SerializedName;

public enum EdgeDirection
{
	
	@SerializedName("NW")
	NorthWest,
	@SerializedName("N")
	North,
	@SerializedName("NE")
	NorthEast,
	@SerializedName("SE")
	SouthEast,
	@SerializedName("S")
	South,
	@SerializedName("SW")
	SouthWest;
	
	private EdgeDirection opposite;
	
	static
	{
		NorthWest.opposite = SouthEast;
		North.opposite = South;
		NorthEast.opposite = SouthWest;
		SouthEast.opposite = NorthWest;
		South.opposite = North;
		SouthWest.opposite = NorthEast;
	}
	
	public EdgeDirection getOppositeDirection()
	{
		return opposite;
	}
	
	public String original(){
		String letters = new String();
		switch (this){
		
		case NorthWest:
			letters = "NW";
		case North:
			letters = "N";
		case NorthEast:
			letters = "NE";
		case SouthEast:
			letters = "SE";
		case South:
			letters = "S";
		case SouthWest:
			letters = "SW";
		}
		return letters;
	}
}

