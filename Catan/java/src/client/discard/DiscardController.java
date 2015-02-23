package client.discard;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.definitions.*;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.DiscardCards;
import shared.proxy.moves.ResourceList;
import client.base.*;
import client.manager.ClientManager;
import client.misc.*;


/**
 * Discard controller implementation
 */
public class DiscardController extends Controller implements IDiscardController, Observer {

	private IWaitView waitView;
	
	private int brickToDiscard = 0;
	private int oreToDiscard = 0;
	private int sheepToDiscard = 0;
	private int wheatToDiscard = 0;
	private int woodToDiscard = 0;
	
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

	//update view after increase/decrease amount called?
	
	@Override
	public void increaseAmount(ResourceType resource) {
		switch(resource) {
			case BRICK:
				brickToDiscard++;
				break;
			case ORE:
				oreToDiscard++;
				break;
			case SHEEP:
				sheepToDiscard++;
				break;
			case WHEAT:
				wheatToDiscard++;
				break;
			case WOOD:
				woodToDiscard++;
				break;
			default: 
				break;
		}
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		switch(resource) {
			case BRICK:
				brickToDiscard--;
				break;
			case ORE:
				oreToDiscard--;
				break;
			case SHEEP:
				sheepToDiscard--;
				break;
			case WHEAT:
				wheatToDiscard--;
				break;
			case WOOD:
				woodToDiscard--;
				break;
			default: 
				break;
		}
	}

	@Override
	public void discard() {
		
		try {
			//called when all conditions are met and user is able and trying to discard the selected cards
			//make a call to server proxy to discard cards
			int playerIndex = ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex();
			
			ResourceList toDiscard = new ResourceList(brickToDiscard, oreToDiscard, sheepToDiscard, wheatToDiscard, woodToDiscard);
			DiscardCards discardReq = new DiscardCards(playerIndex, toDiscard);
			
			ClientManager.instance().getServerProxy().discardCards(discardReq);
			
			//force the model to update right away
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
		
	}

	private void initMaxAmounts() {
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
	
	private int getTotalDiscardNum() {
		return (brickToDiscard + oreToDiscard + sheepToDiscard + wheatToDiscard + woodToDiscard);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		ClientManager cm = ClientManager.instance();
		
		
		if(cm.getCurrentTurnPhase() == TurnPhase.DISCARDING) {
			//show modal when it's in discarding phase
			//if player has more than 8 cards, must discard
			//check that the user hasn't discarded yet
			
			int currPlayerId = cm.getCurrentPlayerInfo().getId();
			
			TurnManager turnManager = cm.getModelFacade().turnManager();
			
			User user = turnManager.getUser(currPlayerId);
			
			int userCardCount = user.getHand().getResourceCards().getAllResourceCards().size(); 
			
			if(userCardCount > 7 && !user.getHasDiscarded()) {
				//initialize the max amounts a player can discard
				initMaxAmounts();
				//get how much user has to discard and set in view
				int needToDiscard = userCardCount/2;
				getDiscardView().setStateMessage("0/" + needToDiscard);
				//show modal
				getDiscardView().showModal();
				
				//once discard is called, set user has discarded to true
				user.setHasDiscarded(true);
				//close the discard modal
				
				//show the wait view modal if it's still discard phase but user has already discarded
				getWaitView().showModal();
			}
			
		}
		//close the wait view modal if it's showing and it's not discard phase anymore
		else if(cm.getCurrentTurnPhase() != TurnPhase.DISCARDING && getWaitView().isModalShowing()) {
			getWaitView().closeModal();
		}
		
	}

}

