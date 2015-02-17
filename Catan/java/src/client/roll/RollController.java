package client.roll;

import java.util.Observable;
import java.util.Observer;

import shared.proxy.moves.RollNumber;
import client.base.*;
import client.manager.ClientManager;
import client.misc.MessageView;


/**
 * Implementation for the roll controller
 */
public class RollController extends Controller implements IRollController, Observer {

	private IRollResultView resultView;

	/**
	 * RollController constructor
	 * 
	 * @param view Roll view
	 * @param resultView Roll result view
	 */
	public RollController(IRollView view, IRollResultView resultView) {

		super(view);
		
		setResultView(resultView);
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
		
		RollNumber param = new RollNumber(1, total);
		
		try {
			ClientManager.getServerProxy().rollNumber(param);
			getResultView().setRollValue(total);
			getResultView().showModal();
		} catch (Exception e) {
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to roll dice. Please try again later.");
			errorMessage.showModal();
		}
		
	}

	@Override
	public void update(Observable o, Object arg) {
		getRollView().closeModal();
	}

}

