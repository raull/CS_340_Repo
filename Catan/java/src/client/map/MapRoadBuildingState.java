package client.map;

import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.Road_Building_;
import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.manager.ClientManager;
import client.misc.MessageView;

public class MapRoadBuildingState extends MapControllerState 
{
	MapRoadBuildingState(MapController controller)
	{
		this.controller = controller;
		roadsPlaced = 0;
	}
	
	private MapController controller;
	private int roadsPlaced;
	private EdgeLocation firstRoad;
	
	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) //TODO may also have to account for the placing a road connected to the first road
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
		return false;
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) 
	{
		return false;
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) 
	{
		return false;
	}

	@Override
	public void run() 
	{
		return;
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{
		return;
	}

	@Override
	public void placeRoad(EdgeLocation edgeLoc) 
	{
		roadsPlaced++;
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		if (roadsPlaced == 1)
		{
			//store the location of the road
			firstRoad = edgeLoc;
			
			//place it on the screen
			controller.getView().placeRoad(edgeLoc, client.getColor());	
			
			//start another drop
			controller.startMove(PieceType.ROAD, true, false);
		}
		else if (roadsPlaced == 2)
		{
			roadsPlaced = 0;
			int clientIndex = client.getPlayerIndex();
			Road_Building_ move = new Road_Building_(clientIndex, firstRoad, edgeLoc);
			
			//place it on the screen
			controller.getView().placeRoad(edgeLoc, client.getColor());	
			
			try {
				ClientManager.instance().getServerProxy().Road_Building(move);
			} catch (ProxyException e) {
				MessageView errorMessage = new MessageView();
				errorMessage.setTitle("Error");
				errorMessage.setMessage("Something wrong happened while trying to play Road Building. Please try again later.");
				errorMessage.showModal();
				//e.printStackTrace();
			}
		}
	}

	@Override
	public void startMove(PieceType type, boolean isFree, boolean allowDisconnected) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		controller.getView().startDrop(type, client.getColor(), true);
	}

	@Override
	public void placeRobber(HexLocation hexLoc) 
	{
		return;
	}
	
	@Override
	public void robPlayer(RobPlayerInfo victim, HexLocation robberLoc) 
	{
		return;
	}

}
