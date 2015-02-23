package client.discard;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.definitions.*;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.DiscardCards;
import client.base.*;
import client.manager.ClientManager;
import client.misc.*;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController, Observer {

	private IWaitView waitView;
	
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		this.waitView = waitView;
		ClientManager.instance().getModelFacade().addObserver(this);
	}

	public IDiscardView getDiscardView() {
		return (IDiscardView)super.getView();
	}
	
	public IWaitView getWaitView() {
		return waitView;
	}

	@Override
	public void increaseAmount(ResourceType resource) {
		
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		
	}

	@Override
	public void discard() {
		
		try {
			//called when all conditions are met and user is able and trying to discard the selected cards
			//make a call to server proxy to discard cards
			//TODO set the request properly
			DiscardCards discardReq = new DiscardCards(-1, null);
			
			ClientManager.instance().getServerProxy().discardCards(discardReq);
			
			JsonElement model = ClientManager.instance().getServerProxy().model(-1);
			ClientManager.instance().getModelFacade().updateModel(model);
			
			getDiscardView().closeModal();
			
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			MessageView alertView = new MessageView();
			alertView.setTitle("Error");
			alertView.setMessage(e.getLocalizedMessage());
			alertView.showModal();
		}

		//force the model to update right away
		
	}

	private void updateMaxAmounts() {
		ClientManager cm = ClientManager.instance();
		
		//get the current user from client manager
		User user = cm.getModelFacade().turnManager().getUserFromIndex(cm.getCurrentPlayerInfo().getPlayerIndex());
		
		int maxBrick = user.getBrickCards();
		int maxOre = user.getOreCards();
		int maxSheep = user.getSheepCards();
		int maxWheat = user.getWheatCards();
		int maxWood = user.getWoodCards();
		
		//update the max amounts of each resource a user can discard
		getDiscardView().setResourceMaxAmount(ResourceType.BRICK, maxBrick);
		getDiscardView().setResourceMaxAmount(ResourceType.ORE, maxOre);
		getDiscardView().setResourceMaxAmount(ResourceType.SHEEP, maxSheep);
		getDiscardView().setResourceMaxAmount(ResourceType.WHEAT, maxWheat);
		getDiscardView().setResourceMaxAmount(ResourceType.WOOD, maxWood);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		ClientManager cm = ClientManager.instance();
		if(cm.getCurrentTurnPhase() == TurnPhase.DISCARDING) {
			//check that the user being selected to discard hasn't discarded yet
			//show modal when it's in discarding phase
			getDiscardView().showModal();
		}
		
		
	}

}

