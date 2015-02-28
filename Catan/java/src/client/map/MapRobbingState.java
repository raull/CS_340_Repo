package client.map;

import java.util.ArrayList;

import shared.definitions.PieceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.board.HexTile;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.RobPlayer;
import client.base.IController;
import client.data.PlayerInfo;
import client.data.RobPlayerInfo;
import client.manager.ClientManager;
import client.misc.MessageView;
import client.state.State;

public class MapRobbingState extends MapControllerState{

	public MapRobbingState(MapController mapController) 
	{
		controller = mapController;
	}
	
	private MapController controller;
	private boolean activeMove = false;

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
		activeMove = true;
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
	public void robPlayer(RobPlayerInfo victim, HexLocation robberLoc) 
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		RobPlayer robplayer = new RobPlayer(client.getPlayerIndex(), victim.getPlayerIndex(), robberLoc);
		try {
			ClientManager.instance().getServerProxy().robPlayer(robplayer);
			activeMove = false;
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			// TODO notify the client of the error and restart a robber drop
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to rob the player. Please try again later.");
			errorMessage.showModal();
			controller.startMove(PieceType.ROBBER, true, true);
			//e.printStackTrace();
		}
	}

	@Override
	public void update() 
	{
		if (!activeMove && !ClientManager.instance().isUserRolling()) 
		{
			run();
		}
	}

}
