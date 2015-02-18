package client.turntracker;

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
		
		User currentUser = ClientManager.instance().getCurrentUser();
		
		//set color of local player
		getView().setLocalPlayerColor(currentUser.getCatanColor());
		
		// initialize the player in turn tracker display
		getView().initializePlayer(currentUser.getTurnIndex(), currentUser.getName(), currentUser.getCatanColor());
		
	}

	@Override
	public void update(Observable o, Object arg) {
		// model facade has changed
		
	}

}

