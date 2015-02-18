package client.map;

import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import client.base.IController;
import client.data.RobPlayerInfo;
import client.state.State;

public class MapRobbingState extends MapControllerState{

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) 
	{
		// TODO logic
		return false;
	}

	@Override
	public void run() 
	{
		//TODO will automatically start the player moving the robber
	}

	@Override
	public void robPlayer(RobPlayerInfo victim) {
		// TODO Auto-generated method stub
		
	}

}
