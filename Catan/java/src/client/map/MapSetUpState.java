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

public class MapSetUpState extends MapControllerState{

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) 
	{
		//TODO will the function call to the canplaceroadatlocation be ok for setup?
		
		ModelFacade facade = ClientManager.instance().getModelFacade();
		User user = ClientManager.instance().getCurrentUser();
		return false;
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) 
	{
		//TODO same concern as above function
		ModelFacade facade = ClientManager.instance().getModelFacade();
		User user = ClientManager.instance().getCurrentUser();
		return facade.canPlaceBuildingAtLoc(facade.turnManager(), vertLoc, user, PieceType.SETTLEMENT);
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) 
	{
		//can't play city during set up
		return false;
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) 
	{
		//can't move robber during setup
		return false;
	}

	@Override
	public void run() 
	{
		//This will automatically start the user placing a road
		//and then automatically start the user placing a settlement
	}

	@Override
	public void robPlayer(RobPlayerInfo victim) {
		// TODO Auto-generated method stub
		
	}

}
