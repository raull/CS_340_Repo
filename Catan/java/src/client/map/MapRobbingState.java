package client.map;

import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.facade.ModelFacade;
import client.base.IController;
import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.manager.ClientManager;
import client.state.State;

public class MapRobbingState extends MapControllerState{

	public MapRobbingState(MapController mapController) 
	{
		controller = mapController;
	}
	
	private MapController controller;

	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) 
	{
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) 
	{
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) 
	{
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) 
	{
		ModelFacade facade = ClientManager.instance().getModelFacade();
		return facade.canPlaceRobberAtLoc(hexLoc);
	}

	@Override
	public void run() 
	{
		//will automatically start the player moving the robber
		controller.startMove(PieceType.ROBBER, true, true);		
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{
		return;
	}

	@Override
	public void placeRoad(EdgeLocation edgeLoc) 
	{
		return;
	}

	@Override
	public void startMove(PieceType type, boolean isFree, boolean allowDisconnected) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		controller.getView().startDrop(type, client.getColor(), false);
	}

	@Override
	public void placeRobber(HexLocation hexLoc) 
	{
		// placeRobber on view
		//initialize robview
	}
	
	@Override
	public void robPlayer(RobPlayerInfo victim) 
	{
		// TODO call rob player on serverProxy	
	}

}
