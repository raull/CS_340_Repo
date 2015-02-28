package client.roll;

import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import shared.model.game.TurnPhase;
import shared.proxy.moves.RollNumber;
import client.base.*;
import client.manager.ClientManager;
import client.misc.MessageView;



/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController, Observer {

	private IRollResultView resultView;
	private Timer rollTimer = new Timer(false);

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {

		super(view);
		
		setResultView(resultView);
		
		//Set as Observer
		ClientManager.instance().getModelFacade().addObserver(this);
	}
	
	public IRollResultView getResultView() {
		return resultView;
	}
	public void setResultView(IRollResultView resultView) {
		this.resultView = resultView;
	}

	public IRollView getRollView() {
		return (IRollView)getView();
	}
	
	@Override
	public void rollDice() {
		
		int dice1 = (int)(Math.random()*6) + 1;
		int dice2 = (int)(Math.random()*6) + 1;
		int total = dice1 + dice2;
		
		RollNumber param = new RollNumber(ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex(), total);
		
		try {
			ClientManager.instance().getServerProxy().rollNumber(param);
			getRollView().closeModal();
			getResultView().setRollValue(total);
			getResultView().showModal();
			rollTimer.cancel();
			ClientManager.instance().forceUpdate();
		} catch (Exception e) {
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to roll dice. Please try again later.");
			errorMessage.showModal();
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		if (ClientManager.instance().getCurrentTurnPhase() == TurnPhase.ROLLING && 
				!getRollView().isModalShowing() && !getResultView().isModalShowing()) {
			getRollView().showModal();
			rollTimer.schedule( new TimerTask() {
				@Override
				public void run() {
					rollDice();
				}
			} , 5000);
			
		}

	}

}

