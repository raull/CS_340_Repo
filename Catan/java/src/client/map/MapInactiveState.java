package client.map;

import client.data.RobPlayerInfo;
import client.manager.ClientManager;
import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.board.Map;
import shared.model.game.TurnManager;

public class MapInactiveState extends MapControllerState {

	public MapInactiveState(MapController mapController) 
	{
		this.controller = mapController;
	}

	private MapController controller;
	
	@Override
	public boolean canPlaceRoad(EdgeLocation edgeLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceSettlement(VertexLocation vertLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceCity(VertexLocation vertLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public boolean canPlaceRobber(HexLocation hexLoc) {
		//If the user isn't active, they can't interact with the map
		return false;
	}

	@Override
	public void run() 
	{
		return;
	}

	@Override
	public void robPlayer(RobPlayerInfo victim, HexLocation robberLoc) 
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
		return;
	}

	@Override
	public void startMove(PieceType type, boolean isFree, boolean allowDisconnected) 
	{
		return;
	}

	@Override
	public void placeRobber(HexLocation hexLoc) 
	{
		return;
	}

	@Override
	public void update() {
		TurnManager turnManager = ClientManager.instance().getModelFacade().turnManager();
		Map map = ClientManager.instance().getModelFacade().map();
		
		controller.updateCities(turnManager, map);
		controller.updateRoads(turnManager, map);
		controller.updateSettlements(turnManager, map);
		controller.updateRobber(turnManager, map);
	}


}
