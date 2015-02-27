package shared.locations;

import com.google.gson.annotations.SerializedName;

public enum VertexDirection
{
	@SerializedName("W")
	West,
	@SerializedName("NW")
	NorthWest, 
	@SerializedName("NE")
	NorthEast, 
	@SerializedName("E")
	East, 
	@SerializedName("SE")
	SouthEast, 
	@SerializedName("SW")
	SouthWest;
	
	
	private VertexDirection opposite;
	
	static
	{
		West.opposite = East;
		NorthWest.opposite = SouthEast;
		NorthEast.opposite = SouthWest;
		East.opposite = West;
		SouthEast.opposite = NorthWest;
		SouthWest.opposite = NorthEast;
	}
	
	public VertexDirection getOppositeDirection()
	{
		return opposite;
	}
	
	public String original(){
		String letters = new String();
		switch (this) {
		case West:
			letters = "W";
			break;
			
		case NorthWest:
			letters = "NW";
			break;
		
		case East:
			letters = "E";
			break;
		
		case NorthEast:
			letters = "NE";
			break;
			
		case SouthEast:
			letters = "SE";
			break;
			
		case SouthWest:
			letters = "SW";
			break;
		}
		return letters;
	}
}

