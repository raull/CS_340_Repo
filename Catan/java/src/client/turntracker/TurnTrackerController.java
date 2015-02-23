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
	
	private boolean updated = false;

	public TurnTrackerController(ITurnTrackerView view) {
		
		super(view);
		//set the local/client player's color
		ClientManager.instance().getModelFacade().addObserver(this);
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
		
//		getView().setLocalPlayerColor(ClientManager.instance().getCurrentPlayerInfo().getColor());
		
		int currPlayerID = ClientManager.instance().getCurrentPlayerInfo().getId();
		
		getView().setLocalPlayerColor(ClientManager.instance().getCurrentGameInfo().getPlayers().get(currPlayerID).getColor());
		
		List<User> users = ClientManager.instance().getModelFacade().getModel().getTurnManager().getUsers();
		
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
			boolean hasLargestArmy = (largestArmyIndex == user.getTurnIndex());
			boolean hasLongestRoad = (longestRoadIndex == user.getTurnIndex());
//			System.out.println( "current player index: " + user.getTurnIndex() + " player: " + " largestArmyIndex" + " has largest army? " + hasLargestArmy);
			
			getView().updatePlayer(user.getTurnIndex(), user.getVictoryPoints(), isHighlighted, hasLargestArmy, hasLongestRoad);
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// model facade has changed
		//update the turntracker view for all players
		//if users are null, init from model (or, has never been updated yet)
		//also, only initialize when all players have joined
		if(!updated &&
				ClientManager.instance().getCurrentGameInfo().getPlayers().size() == 4) {
			updated = true;
			initFromModel();
		}
		//else if there are 4 players, update
		else if(ClientManager.instance().getCurrentGameInfo().getPlayers().size() == 4){
			updatePlayers();
		}
		
		
	}

}

