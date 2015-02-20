package client.turntracker;

import java.util.Observable;
import java.util.Observer;

import shared.model.game.User;

import client.base.*;
import client.data.PlayerInfo;
import client.manager.ClientManager;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {

	public TurnTrackerController(ITurnTrackerView view) {
		
		super(view);
		
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {

	}
	
	private void initFromModel() {
		
		PlayerInfo playerInfo = ClientManager.instance().getCurrentPlayerInfo();
		
		//set color of local player
		getView().setLocalPlayerColor(playerInfo.getColor());
		
		// initialize the player in turn tracker display
		getView().initializePlayer(playerInfo.getPlayerIndex(), playerInfo.getName(), playerInfo.getColor());
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// model facade has changed
		ClientManager cm = ClientManager.instance();
		
		PlayerInfo playerInfo = cm.getCurrentPlayerInfo();
		
		User currUser = cm.getModelFacade().turnManager().getUser(playerInfo.getPlayerIndex());
		
		int largestArmyIndex = cm.getModelFacade().score().getLargestArmyUser();
		int longestRoadIndex = cm.getModelFacade().score().getLongestRoadUser();
		
		//user is highlighted if it's currently their turn
		boolean isHighlighted = (cm.getModelFacade().turnManager().getCurrentTurn() == playerInfo.getId());
		
		//booleans for if user has largest army or longest road
		boolean hasLargestArmy = (largestArmyIndex == playerInfo.getPlayerIndex());
		boolean hasLongestRoad = (longestRoadIndex == playerInfo.getPlayerIndex());
		
		//update the user
		getView().updatePlayer(playerInfo.getPlayerIndex(), currUser.getVictoryPoints(), isHighlighted, hasLargestArmy, hasLongestRoad);
	}

}

