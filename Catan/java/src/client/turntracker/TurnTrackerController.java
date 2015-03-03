package client.turntracker;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.FinishMove;
import client.base.*;
import client.manager.ClientManager;
import client.misc.MessageView;


/**
 * Implementation for the turn tracker controller
 */
public class TurnTrackerController extends Controller implements ITurnTrackerController, Observer {
	
	private boolean updated = false;
	private int PLAYER_COUNT = 4;

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
		//if user ends turn, update their booleans
		int currPlayerId = ClientManager.instance().getCurrentPlayerInfo().getId();
		User currUser = ClientManager.instance().getModelFacade().turnManager().getUser(currPlayerId);
		
		//probably already reset in server
//		currUser.setHasPlayedDevCard(false);
//		currUser.setHasDiscarded(false);
		
		if(ClientManager.instance().getModelFacade().canFinishTurn(ClientManager.instance().getModelFacade().turnManager(), currUser)) {
			
			try {
				FinishMove finishMoveReq = new FinishMove(currUser.getTurnIndex());
				ClientManager.instance().getServerProxy().finishTurn(finishMoveReq);
				
				//force an update from model immediately
				JsonElement model = ClientManager.instance().getServerProxy().model(-1);
				ClientManager.instance().getModelFacade().updateModel(model);
			} catch (ProxyException e) {
				e.printStackTrace();
				MessageView alertView = new MessageView();
				alertView.setTitle("Error");
				alertView.setMessage(e.getLocalizedMessage());
				alertView.showModal();
			}
			
			
		}
		else{
			//can't finish turn
		}
		
		
		
	}
	
	private void initFromModel() {
		
		int currPlayerIndex = ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex();
		
		getView().setLocalPlayerColor(ClientManager.instance().getCurrentGameInfo().
				getPlayers().get(currPlayerIndex).getColor());
		
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
		System.out.println("longest road person: " + longestRoadIndex);
		
		for(User user : users) {
			//user is highlighted if it's currently their turn
			boolean isHighlighted = (cm.getModelFacade().turnManager().getCurrentTurn() == user.getTurnIndex());
			
			//booleans for if user has largest army or longest road
			boolean hasLargestArmy = (largestArmyIndex == user.getTurnIndex());
			boolean hasLongestRoad = (longestRoadIndex == user.getTurnIndex());
			
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
				ClientManager.instance().getCurrentGameInfo().getPlayers().size() == PLAYER_COUNT) {
			
			initFromModel();
			updated = true;
		}
		//else if there are 4 players, update
		else if(ClientManager.instance().getCurrentGameInfo().getPlayers().size() == PLAYER_COUNT){
			updatePlayers();
		}
		TurnManager turnManager = ClientManager.instance().getModelFacade().turnManager();
		int id = ClientManager.instance().getCurrentPlayerInfo().getId();
		if(ClientManager.instance().getModelFacade().canFinishTurn(turnManager, turnManager.getUserFromID(id))){
//			System.out.println("Can end turn, setting button");
			this.getView().updateGameState("End Turn", true);
		}
		else {
			this.getView().updateGameState("Waiting for other players", false);
		}
		
		
	}

}

