package client.join;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.ServerProxy;
import shared.proxy.game.AddAIRequest;
import client.base.*;
import client.data.PlayerInfo;
import client.manager.ClientManager;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController, Observer {

	public PlayerWaitingController(IPlayerWaitingView view) {
		super(view);
		ClientManager.instance().getModelFacade().addObserver(this);
		view.setAIChoices(new String[]{"Largest Army"});
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
		getView().showModal();
		attemptClose(); //immediately closes the modal if there's nothing to do
	}

	@Override
	public void addAI() {
		
		try {
			AddAIRequest req = new AddAIRequest("LARGEST_ARMY"); //only type of AI supported by current server
			//add AI from proxy
			ClientManager.instance().getServerProxy().addAI(req);
			JsonElement model = ClientManager.instance().getServerProxy().model(-1);
			ClientManager.instance().getModelFacade().updateModel(model);
			attemptClose();
			
		} catch (ProxyException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientManager cm = ClientManager.instance();
		for(User u : cm.getModelFacade().turnManager().getUsers()){ //iterates through all players
			PlayerInfo newPlayer = new PlayerInfo();
			newPlayer.setColor(u.getCatanColor());
			newPlayer.setId(u.getPlayerID());
			newPlayer.setName(u.getName());
			newPlayer.setPlayerIndex(u.getTurnIndex());
			
			for(PlayerInfo pi : cm.getCurrentGameInfo().getPlayers()){ //checks all players already known about
				if(pi.getName().equals(u.getName())){ //if that player is already known, we don't need to add them
					newPlayer = null;
					break;
				}
			}
			if(newPlayer!=null){ //if the player wasn't found, add him/her to the game
				cm.getCurrentGameInfo().addPlayer(newPlayer);
			}
		}
		attemptClose();
	}
	
	private void attemptClose(){
		if(ClientManager.instance().getCurrentGameInfo().getPlayers().size()==4 
				&& this.getView().isModalShowing()){ //modal only closes if there are four players
			getView().closeModal();
			//TODO start the game
		}
	}

}

