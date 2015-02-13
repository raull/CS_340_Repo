package client.map;

import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.facade.ModelFacade;
import shared.model.game.User;

public class MapActiveState extends MapControllerState{

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		//THIS IS WRONG, NEEDS TO BE CHANGED
		ModelFacade tempFacade = new ModelFacade();
		User tempUser = new User();
		return tempFacade.canPlaceRoadAtLoc(tempFacade.turnManager(), edgeLoc, tempUser);
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

}
