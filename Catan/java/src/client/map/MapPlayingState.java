package client.map;

import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.manager.ClientManager;
import client.misc.MessageView;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.board.Map;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.BuildRoad;
import shared.proxy.moves.BuildSettlement;
import shared.proxy.moves.Soldier_;

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
	public void robPlayer(RobPlayerInfo victim, HexLocation robberLoc) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		Soldier_ move = new Soldier_(client.getPlayerIndex(), victim.getPlayerIndex(), robberLoc);
		
		try {
			ClientManager.instance().getServerProxy().Soldier(move);
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to play the Soldier card. Please try again later.");
			errorMessage.showModal();
			//e.printStackTrace();
		}
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{		
		PlayerInfo info = ClientManager.instance().getCurrentPlayerInfo();
		ModelFacade facade = ClientManager.instance().getModelFacade();
		TurnManager turnManager = facade.turnManager();
		User client = turnManager.getUserFromIndex(info.getPlayerIndex());
		controller.getView().placeSettlement(vertLoc, client.getCatanColor());
		BuildSettlement buildsettlement = new BuildSettlement(client.getTurnIndex(), vertLoc, false);
		try {
			ClientManager.instance().getServerProxy().buildSettlement(buildsettlement);
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to place the settlement. Please try again later.");
			errorMessage.showModal();
			//e.printStackTrace();
		}	
	}

	@Override
	public void placeRoad(EdgeLocation edgeLoc) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		controller.getView().placeRoad(edgeLoc, client.getColor());	
		int clientIndex = client.getPlayerIndex();
		BuildRoad buildroad = new BuildRoad(clientIndex, edgeLoc, false);
		
		try {
			ClientManager.instance().getServerProxy().buildRoad(buildroad);
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to place the road. Please try again later.");
			errorMessage.showModal();
			e.printStackTrace();
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
		
	}

	@Override
	public void update() {
		
		TurnManager turnManager = ClientManager.instance().getModelFacade().turnManager();
		Map map = ClientManager.instance().getModelFacade().map();
		
		controller.updateRobber(turnManager, map);
		controller.updateCities(turnManager, map);
		controller.updateSettlements(turnManager, map);
		controller.updateRoads(turnManager, map);
		
	}

}
