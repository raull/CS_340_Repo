package client.turntracker;

import java.awt.Color;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.CatanColor;
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
		//<temp>
		CatanColor currentColor = ClientManager.instance().getCurrentUser().getCatanColor();
		getView().setLocalPlayerColor(currentColor);
		//</temp>
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

}

