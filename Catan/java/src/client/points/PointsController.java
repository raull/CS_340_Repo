package client.points;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.model.game.User;

import client.base.*;
import client.data.PlayerInfo;
import client.manager.ClientManager;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController, Observer {

	private IGameFinishedView finishedView;
	private int PLAYER_COUNT = 4;
	/**
	 * PointsController constructor
	 * 
	 * @param view Points view
	 * @param finishedView Game finished view, which is displayed when the game is over
	 */
	public PointsController(IPointsView view, IGameFinishedView finishedView) {
		
		super(view);
		
		setFinishedView(finishedView);
		
		initFromModel();
		
		ClientManager.instance().getModelFacade().addObserver(this);
	}
	
	public IPointsView getPointsView() {
		
		return (IPointsView)super.getView();
	}
	
	public IGameFinishedView getFinishedView() {
		return finishedView;
	}
	public void setFinishedView(IGameFinishedView finishedView) {
		this.finishedView = finishedView;
	}

	private void initFromModel() {
		
		//initialize points to 0
		getPointsView().setPoints(0);
		
	}
	
	private void updatePoints(boolean hasWinner) {
		ClientManager cm = ClientManager.instance();
		
		//get information of what client user's turn index is
//		int currPlayerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
		int currPlayerID = cm.getCurrentPlayerInfo().getId();
		
		//get the user from the turn index
//		User user = cm.getModelFacade().turnManager().getUser(currPlayerIndex);
		//get user by id
		User user = cm.getModelFacade().turnManager().getUser(currPlayerID);
		
		//set the user's number of points
		getPointsView().setPoints(user.getVictoryPoints());
		
		if(hasWinner) {
			int winnerIndex = cm.getModelFacade().getWinnerIndex();
			int currPlayerIndex = user.getTurnIndex();
			boolean isLocalPlayer = (winnerIndex == currPlayerIndex);
			String winnerName = cm.getModelFacade().turnManager().getUserFromIndex(winnerIndex).getName();
			getFinishedView().setWinner(winnerName, isLocalPlayer);
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientManager cm = ClientManager.instance();
		//when there are 4 players that has joined, then update
		if(cm.getCurrentGameInfo().getPlayers().size() == PLAYER_COUNT) {
			//if there is a winner
			boolean hasWinner = false;
			if(cm.getModelFacade().getModel().getWinner() != -1) {
				hasWinner = true;
			}
			updatePoints(hasWinner);
		}
 
		
	}
	
}

