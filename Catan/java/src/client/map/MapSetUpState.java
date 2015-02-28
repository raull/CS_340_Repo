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
import shared.proxy.moves.FinishMove;

public class MapSetUpState extends MapControllerState{

	public MapSetUpState(MapController mapController) 
	{
		controller = mapController;
	}

	private MapController controller;
	private boolean activeMove = false;
	
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
		activeMove = true;
		startMove(PieceType.ROAD, true, true);
	}

	@Override
	public void placeSettlement(VertexLocation vertLoc) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		//controller.getView().placeSettlement(vertLoc, client.getColor());
		BuildSettlement buildsettlement = new BuildSettlement(client.getPlayerIndex(), vertLoc, true);
		try {
			ClientManager.instance().getServerProxy().buildSettlement(buildsettlement);
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			// TODO notify the user that there was an error and restart a settlement drop
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to place the settlement. Please try again later.");
			errorMessage.showModal();
			controller.startMove(PieceType.SETTLEMENT, true, false);
		}
		
		FinishMove finish = new FinishMove(client.getPlayerIndex());
		try {
			ClientManager.instance().getServerProxy().finishTurn(finish);
			activeMove = false;
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to end your turn. Please try again later.");
			errorMessage.showModal();
		}
	}

	@Override
	public void placeRoad(EdgeLocation edgeLoc) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		BuildRoad buildroad = new BuildRoad(client.getPlayerIndex(), edgeLoc, true);
		
		try {
			ClientManager.instance().getServerProxy().buildRoad(buildroad);
			ClientManager.instance().forceUpdate();
			controller.startMove(PieceType.SETTLEMENT, true, false);
		} catch (ProxyException e) {
			// TODO probably want to notify the client that there was an error and then restart a road drop
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to place the road. Please try again later.");
			errorMessage.showModal();
			controller.startMove(PieceType.ROAD, true, true);
		}
		
		
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
		return;
	}
	
	@Override
	public void robPlayer(RobPlayerInfo victim, HexLocation robberLoc) 
	{
		return;
	}
	
	@Override
	public void update() 
	{
		TurnManager turnManager = ClientManager.instance().getModelFacade().turnManager();
		Map map = ClientManager.instance().getModelFacade().map();
		
		controller.updateRoads(turnManager, map);
		controller.updateSettlements(turnManager, map);
		
		if (!activeMove && ClientManager.instance().hasGameStarted()) {
			run();
		}
	}

}
