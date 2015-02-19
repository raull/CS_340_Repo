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

public class MapPlayingState extends MapControllerState
{

	MapPlayingState(MapController controller)
	{
		this.controller = controller;
	}
	
	private MapController controller;
	
	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) 
	{
		ModelFacade facade = ClientManager.instance().getModelFacade();
		PlayerInfo info = ClientManager.instance().getCurrentPlayerInfo();
		TurnManager turnManager = facade.turnManager();
		User user = turnManager.getUserFromIndex(info.getPlayerIndex());
		return facade.canPlaceRoadAtLoc(facade.turnManager(), edgeLoc, user);
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) 
	{	
		ModelFacade facade = ClientManager.instance().getModelFacade();
		PlayerInfo info = ClientManager.instance().getCurrentPlayerInfo();
		TurnManager turnManager = facade.turnManager();
		User user = turnManager.getUserFromIndex(info.getPlayerIndex());
		return facade.canPlaceBuildingAtLoc(facade.turnManager(), vertLoc, user, PieceType.SETTLEMENT);
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) 
	{
		ModelFacade facade = ClientManager.instance().getModelFacade();
		PlayerInfo info = ClientManager.instance().getCurrentPlayerInfo();
		TurnManager turnManager = facade.turnManager();
		User user = turnManager.getUserFromIndex(info.getPlayerIndex());
		return facade.canPlaceBuildingAtLoc(facade.turnManager(), vertLoc, user, PieceType.CITY);
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
		return;
	}

	@Override
	public void robPlayer(RobPlayerInfo victim) 
	{
		// TODO This is going to be called when playing a Soldier card	
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{		
		//TODO decide what to do on ProxyException
		PlayerInfo info = ClientManager.instance().getCurrentPlayerInfo();
		ModelFacade facade = ClientManager.instance().getModelFacade();
		TurnManager turnManager = facade.turnManager();
		User client = turnManager.getUserFromIndex(info.getPlayerIndex());
		controller.getView().placeSettlement(vertLoc, client.getCatanColor());
		BuildSettlement buildsettlement = new BuildSettlement(client.getTurnIndex(), vertLoc, false);
		try {
			ClientManager.instance().getServerProxy().buildSettlement(buildsettlement);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

}
