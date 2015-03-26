package client.points;

import java.util.Observable;
import java.util.Observer;

import shared.model.game.User;
import client.base.*;
import client.join.JoinGameController;
import client.join.JoinGameView;
import client.join.NewGameView;
import client.join.SelectColorView;
import client.manager.ClientManager;
import client.misc.MessageView;


/**
 * Implementation for the points controller
 */
public class PointsController extends Controller implements IPointsController, Observer {

	private IGameFinishedView finishedView;
	private int PLAYER_COUNT = 4;
	private boolean displayed;
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
		
		displayed = false;
		
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
		int currPlayerId = cm.getCurrentPlayerInfo().getId();
		int currPlayerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
		//get the user from the turn index
//		
		//get user by id
//		User user = cm.getModelFacade().turnManager().getUser(currPlayerID);
		User user = cm.getModelFacade().turnManager().getUserFromIndex(currPlayerIndex);
		//set the user's number of points
		getPointsView().setPoints(user.getVictoryPoints());
		
		if(hasWinner) {
			/////////////////////
			//winner is returning player id, not turn index, as the specs say
			////////////////////
			System.out.println("has winner");
			//int winnerIndex = cm.getModelFacade().getWinnerIndex();
			int winnerId = cm.getModelFacade().score().getWinner();
			//boolean isLocalPlayer = (winnerIndex == currPlayerIndex);
			boolean isLocalPlayer = (winnerId == currPlayerId);
			
			//String winnerName = cm.getModelFacade().turnManager().getUserFromIndex(winnerIndex).getName();
//			String winnerName = cm.getModelFacade().turnManager().getUserFromID(winnerId).getName();
			String winnerName = cm.getModelFacade().turnManager().getUserFromID(winnerId).getName();
			System.out.println("winner: " + winnerName + " local player?? " + isLocalPlayer);
			getFinishedView().setWinner(winnerName, isLocalPlayer);
			if (!displayed){
				getFinishedView().showModal();
				displayed = true;
			}
			
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		ClientManager cm = ClientManager.instance();
		//when there are 4 players that has joined, then update
		if(cm.getCurrentGameInfo().getPlayers().size() == PLAYER_COUNT) {
			
			//if there is a winner
			boolean hasWinner = false;
//			System.out.println("winner???? " + cm.getModelFacade().score().getWinner());
			if(cm.getModelFacade().score().getWinner() != -1) {
				hasWinner = true;
			}
			updatePoints(hasWinner);
		}
 
		
	}
	
}

