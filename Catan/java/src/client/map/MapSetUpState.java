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
import shared.model.game.TurnPhase;
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
	private final int MAX_ROADS = 15;
	private final int MAX_SETTLEMENTS = 5;
	
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
		System.out.println("Setting active move to true");
		activeMove = true;
		TurnManager turnManager = ClientManager.instance().getModelFacade().turnManager();

		if (ClientManager.instance().getCurrentTurnPhase() == TurnPhase.FIRSTROUND)
		{
			if (turnManager.currentUser().getUnusedRoads() == MAX_ROADS)
			{
				System.out.println("starting first round drop");
				startMove(PieceType.ROAD, true, true);
			}
			else if (turnManager.currentUser().getUnusedSettlements() == MAX_SETTLEMENTS)
			{
				controller.startMove(PieceType.SETTLEMENT, true, false);
			}
			else
			{
				assert false;
			}
		}
		if (ClientManager.instance().getCurrentTurnPhase() == TurnPhase.SECONDROUND)
		{
			if (turnManager.currentUser().getUnusedRoads() == MAX_ROADS - 1)
			{
				System.out.println("starting second round drop");
				startMove(PieceType.ROAD, true, true);
			}
			else if (turnManager.currentUser().getUnusedSettlements() == MAX_SETTLEMENTS -1)
			{
				controller.startMove(PieceType.SETTLEMENT, true, false);
			}
			else
			{
				assert false;
			}
		}
		
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
			System.out.println("setting active move to false");
			ClientManager.instance().forceUpdate();
			activeMove = false;
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
		System.out.println("Setup State active move: " + activeMove);
		if (!activeMove && ClientManager.instance().hasGameStarted()) {
			run();
		}
	}

}
