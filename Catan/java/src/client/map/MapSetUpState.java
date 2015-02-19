package client.map;

import client.base.IController;
import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.manager.ClientManager;
import client.state.State;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.BuildSettlement;

public class MapSetUpState extends MapControllerState{

	public MapSetUpState(MapController mapController) 
	{
		controller = mapController;
	}

	private MapController controller;
	
	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) 
	{		
		ModelFacade facade = ClientManager.instance().getModelFacade();
		TurnManager turnManager = facade.turnManager();
		PlayerInfo user = ClientManager.instance().getCurrentPlayerInfo();
		return facade.canPlaceRoadAtLoc(turnManager, edgeLoc, turnManager.getUserFromIndex(user.getPlayerIndex()));
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) 
	{
		ModelFacade facade = ClientManager.instance().getModelFacade();
		TurnManager turnManager = facade.turnManager();
		PlayerInfo info = ClientManager.instance().getCurrentPlayerInfo();
		User user = turnManager.getUserFromIndex(info.getPlayerIndex());
		return facade.canPlaceBuildingAtLoc(turnManager, vertLoc, user, PieceType.SETTLEMENT);
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
	public void robPlayer(RobPlayerInfo victim) 
	{
		return;
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		controller.getView().placeSettlement(vertLoc, client.getColor());
		BuildSettlement buildsettlement = new BuildSettlement(client.getPlayerIndex(), vertLoc, true);
		try {
			ClientManager.instance().getServerProxy().buildSettlement(buildsettlement);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
