package client.map;

import client.base.IController;
import client.data.RobPlayerInfo;
import client.manager.ClientManager;
import client.state.State;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.facade.ModelFacade;
import shared.model.game.User;

public class MapPlayingState extends MapControllerState
{

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) 
	{
		ModelFacade facade = ClientManager.instance().getModelFacade();
		User user = ClientManager.instance().getCurrentUser();
		return facade.canPlaceRoadAtLoc(facade.turnManager(), edgeLoc, user);
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) 
	{	
		ModelFacade facade = ClientManager.instance().getModelFacade();
		User user = ClientManager.instance().getCurrentUser();
		return facade.canPlaceBuildingAtLoc(facade.turnManager(), vertLoc, user, PieceType.SETTLEMENT);
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) 
	{
		ModelFacade facade = ClientManager.instance().getModelFacade();
		User user = ClientManager.instance().getCurrentUser();
		return facade.canPlaceBuildingAtLoc(facade.turnManager(), vertLoc, user, PieceType.CITY);
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) 
	{
		//not quite sure what to use for this one just yet
		return false;
	}

	@Override
	public void run() 
	{
		return;
	}

	@Override
	public void robPlayer(RobPlayerInfo victim) {
		// TODO Auto-generated method stub
		
	}

}
