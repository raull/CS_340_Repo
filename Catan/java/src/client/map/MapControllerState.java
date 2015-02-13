package client.map;

import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;

public abstract class MapControllerState {

	
	public abstract boolean canPlaceRoad(EdgeLocation edgeLoc);
	
	public abstract boolean canPlaceSettlement(VertexLocation vertLoc);
	
	public abstract boolean canPlaceCity(VertexLocation vertLoc);
	
	public abstract boolean canPlaceRobber(HexLocation hexLoc);
	
	
	
}
