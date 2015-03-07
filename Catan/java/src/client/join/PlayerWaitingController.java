package client.join;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.game.AddAIRequest;
import client.base.*;
import client.data.PlayerInfo;
import client.manager.ClientManager;
import client.misc.MessageView;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController, Observer {

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
		ClientManager.instance().getModelFacade().addObserver(this);
		getView().setAIChoices(new String[]{"Largest Army"});
		updatePlayers();
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
		
		ClientManager.instance().forceUpdate();
		updatePlayers();
		
		if (isFull()) {
			OverlayView.closeAllModals();
			ClientManager.instance().startGame();
		} 
		else {
			getView().showModal();
		}
		
		
		
		
	}

	@Override
	public void addAI() {
		
		try {
			AddAIRequest req = new AddAIRequest("LARGEST_ARMY"); //only type of AI supported by current server
			//add AI from proxy
			ClientManager.instance().getServerProxy().addAI(req);
			ClientManager.instance().forceUpdate();
			
		} catch (ProxyException e) {
			MessageView alertView = new MessageView();
			alertView.setTitle("Error");
			alertView.setMessage(e.getLocalizedMessage());
			alertView.showModal();
		}
		
		
	}

	@Override
	public void update(Observable o, Object arg) {
		
		boolean updated = false;
		
		ClientManager cm = ClientManager.instance();
		for(User u : cm.getModelFacade().turnManager().getUsers()){ //iterates through all players
			PlayerInfo newPlayer = new PlayerInfo();
			newPlayer.setColor(u.getCatanColor());
			newPlayer.setId(u.getPlayerID());
			newPlayer.setName(u.getName());
			System.out.println("player waiting...curr user's turn index: " + u.getTurnIndex());
			newPlayer.setPlayerIndex(u.getTurnIndex());
			
			for(PlayerInfo pi : cm.getCurrentGameInfo().getPlayers()){ //checks all players already known about
				if(pi.getName().equals(u.getName())){ //if that player is already known, we don't need to add them
					newPlayer = null;
					if(!pi.getColor().equals(u.getCatanColor())){
						updated = true;
						pi.setColor(u.getCatanColor()); //in case colors have changed
					}
					break;
				}
			}
			if(newPlayer!=null){ //if the player wasn't found, add him/her to the game
				updated = true;
				cm.getCurrentGameInfo().addPlayer(newPlayer);
			}
		}
		
//		for(User u : cm.getModelFacade().turnManager().getUsers()) { //iterate through all players
//			PlayerInfo newPlayer = new PlayerInfo();
//			newPlayer.setColor(u.getCatanColor());
//			newPlayer.setId(u.getPlayerID());
//			newPlayer.setName(u.getName());
//			newPlayer.setPlayerIndex(u.getTurnIndex());
//			
//			//check if this new player already exists in current game info
//			List<PlayerInfo> currPlayers = cm.getCurrentGameInfo().getPlayers();
//			
//			int currPlayerIndex = currPlayers.indexOf(newPlayer);
//			//if current player already exists
//			if(currPlayerIndex != -1) {
//				//set color again just in case it changed
//				currPlayers.get(currPlayerIndex).setColor(u.getCatanColor());
//				//also set turn index if it changed? not sure why it would but
//				currPlayers.get(currPlayerIndex).setPlayerIndex(u.getTurnIndex());
//			}
//			else{
//				//add new player to current game info
//				cm.getCurrentGameInfo().addPlayer(newPlayer);
//			}
//			updated = true;
//					
//		}
		
		updatePlayers();
		
		if(isFull() && getView().isModalShowing() && !ClientManager.instance().hasGameStarted()) {
			OverlayView.closeAllModals();
			ClientManager.instance().startGame();
		} else if (updated && !ClientManager.instance().hasGameStarted() && !isFull()){
			getView().closeModal();
			getView().showModal();
		}
		
	}
	

	private boolean isFull(){
		if(ClientManager.instance().getCurrentGameInfo().getPlayers().size()>=4){ //modal only closes if there are four or more players
			return true;
		}
		return false;
	}
	
	private void updatePlayers() {
		ArrayList<PlayerInfo> players =  new ArrayList<PlayerInfo>(ClientManager.instance().getCurrentGameInfo().getPlayers());
		PlayerInfo [] playerArray = players.toArray(new PlayerInfo[players.size()]);
		getView().setPlayers(playerArray);
	}

}

