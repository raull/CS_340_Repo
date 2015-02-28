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
			break;
		case North:
			letters = "N";
			break;
		case NorthEast:
			letters = "NE";
			break;
		case SouthEast:
			letters = "SE";
			break;
		case South:
			letters = "S";
			break;
		case SouthWest:
			letters = "SW";
			break;
		}
		return letters;
	}
}

