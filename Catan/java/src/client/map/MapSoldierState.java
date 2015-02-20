package client.map;

import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import client.data.RobPlayerInfo;

public class MapSoldierState extends MapControllerState 
{

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void robPlayer(RobPlayerInfo victim) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void placeRoad(EdgeLocation edgeLoc) {
		// TODO Auto-generated method stub
		
	}

}
