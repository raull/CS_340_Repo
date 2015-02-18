package client.join;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.proxy.ProxyException;
import shared.proxy.ServerProxy;
import shared.proxy.game.AddAIRequest;

import client.base.*;
import client.manager.ClientManager;


/**
 * Implementation for the player waiting controller
 */
public class PlayerWaitingController extends Controller implements IPlayerWaitingController, Observer {

	public PlayerWaitingController(IPlayerWaitingView view) {

		super(view);
	}

	@Override
	public IPlayerWaitingView getView() {

		return (IPlayerWaitingView)super.getView();
	}

	@Override
	public void start() {
		
		getView().showModal();
	}

	@Override
	public void addAI() {
		
		try {
			AddAIRequest req = new AddAIRequest("LARGEST_ARMY"); //only type of AI supported by current server
			//add AI from proxy
			ClientManager.instance().getServerProxy().addAI(req);
			
			getView().closeModal();
		} catch (ProxyException e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		getView().closeModal();
	}

}

