package shared.proxy.moves;


import com.google.gson.annotations.SerializedName;

public enum comVertexDir
{
	@SerializedName("West")
	W,
	@SerializedName("NorthWest")
	NW, 
	@SerializedName("NorthEast")
	NE, 
	@SerializedName("East")
	E, 
	@SerializedName("SouthEast")
	SE, 
	@SerializedName("SouthWest")
	SW;
}
