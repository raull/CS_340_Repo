package client.turntracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.model.game.User;

import client.base.*;
import client.manager.ClientManager;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {

	public TurnTrackerController(ITurnTrackerView view) {
		
		super(view);
		//set the local/client player's color
		ClientManager.instance().getModelFacade().addObserver(this);
		getView().setLocalPlayerColor(ClientManager.instance().getCurrentPlayerInfo().getColor());
		initFromModel();
	}
	
	@Override
	public ITurnTrackerView getView() {
		
		return (ITurnTrackerView)super.getView();
	}

	@Override
	public void endTurn() {
		//ending turn would un-highlight current player?
		//currently just update the players
		updatePlayers();
	}
	
	private void initFromModel() {
		
		List<User> users = ClientManager.instance().getModelFacade().getModel().getTurnManager().getUsers();
		
		System.out.println("user size: " + users.size());
		
		//initialize players from turn manager
		for(User user : users) {
			getView().initializePlayer(user.getTurnIndex(), user.getName(), user.getCatanColor());
		}

		
	}
	
	private void updatePlayers() {
		ClientManager cm = ClientManager.instance();
		
		ArrayList<User> users = new ArrayList<User>(cm.getModelFacade().turnManager().getUsers());
		
		int largestArmyIndex = cm.getModelFacade().score().getLargestArmyUser();
		int longestRoadIndex = cm.getModelFacade().score().getLongestRoadUser();
		
		for(User user : users) {
			//user is highlighted if it's currently their turn
			boolean isHighlighted = (cm.getModelFacade().turnManager().getCurrentTurn() == user.getPlayerID());
			
			//booleans for if user has largest army or longest road
			boolean hasLargestArmy = (largestArmyIndex == user.getPlayerID());
			boolean hasLongestRoad = (longestRoadIndex == user.getPlayerID());
			
			getView().updatePlayer(user.getTurnIndex(), user.getVictoryPoints(), isHighlighted, hasLargestArmy, hasLongestRoad);
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// model facade has changed
		//update the turntracker view for all players
		updatePlayers();
	}

}

