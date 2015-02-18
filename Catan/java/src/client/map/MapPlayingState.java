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
	public void robPlayer(RobPlayerInfo victim) 
	{
		// TODO This is going to be called when playing a Soldier card	
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{		
		//TODO decide what to do on ProxyException
		User client = ClientManager.instance().getCurrentUser();
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
