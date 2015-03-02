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
	
	private ClientManager cm = ClientManager.instance();
	
	private boolean isDiscarding = false;
	
	private int brickToDiscard = 0;
	private int oreToDiscard = 0;
	private int sheepToDiscard = 0;
	private int wheatToDiscard = 0;
	private int woodToDiscard = 0;
	private int needToDiscard = 0;
	/**
	 * DiscardController constructor
	 * 
	 * @param view View displayed to let the user select cards to discard
	 * @param waitView View displayed to notify the user that they are waiting for other players to discard
	 */
	public DiscardController(IDiscardView view, IWaitView waitView) {
		
		super(view);
		
		this.waitView = waitView;
		cm.getModelFacade().addObserver(this);
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
		int amount = 0;
		switch(resource) {
			case BRICK:
				brickToDiscard++;
				amount = brickToDiscard;
				break;
			case ORE:
				oreToDiscard++;
				amount = oreToDiscard;
				break;
			case SHEEP:
				sheepToDiscard++;
				amount = sheepToDiscard;
				break;
			case WHEAT:
				wheatToDiscard++;
				amount = wheatToDiscard;
				break;
			case WOOD:
				woodToDiscard++;
				amount = woodToDiscard;
				break;
			default: 
				break;
		}
		getDiscardView().setResourceDiscardAmount(resource, amount);
		//setResourceChangeEnabled(resource);
		//go through all resourcs to set enable/disable
		initResourceChangeEnabled();
		getDiscardView().setStateMessage("Discard: " + getTotalDiscardNum() + "/" + needToDiscard);
		checkCanDiscard();
	}

	@Override
	public void decreaseAmount(ResourceType resource) {
		int amount = 0;
		switch(resource) {
			case BRICK:
				brickToDiscard--;
				amount = brickToDiscard;
				break;
			case ORE:	
				oreToDiscard--;
				amount = oreToDiscard;
				break;
			case SHEEP:
				sheepToDiscard--;
				amount = sheepToDiscard;
				break;
			case WHEAT:
				wheatToDiscard--;
				amount = wheatToDiscard;
				break;
			case WOOD:
				woodToDiscard--;
				amount = woodToDiscard;
				break;
			default: 
				break;
		}
		getDiscardView().setResourceDiscardAmount(resource, amount);
		//setResourceChangeEnabled(resource);
		//go through all resourcs to set enable/disable
		initResourceChangeEnabled();
		getDiscardView().setStateMessage("Discard: " + getTotalDiscardNum() + "/" + needToDiscard);
		checkCanDiscard();
	}

	@Override
	public void discard() {
		
		try {
			//called when all conditions are met and user is able and trying to discard the selected cards
			//make a call to server proxy to discard cards
			int playerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
			
			ResourceList toDiscard = new ResourceList(brickToDiscard, oreToDiscard, sheepToDiscard, wheatToDiscard, woodToDiscard);
			DiscardCards discardReq = new DiscardCards(playerIndex, toDiscard);
			
			cm.getServerProxy().discardCards(discardReq);
			//once discard is called, set user has discarded to true
			cm.getModelFacade().turnManager().getUserFromIndex(playerIndex).setHasDiscarded(true);
			
			//force the model to update right away
			JsonElement model = cm.getServerProxy().model(-1);
			cm.getModelFacade().updateModel(model);
			
			//reset the cards so next time the cards won't be messed up
			resetToDiscard();
			
			//close the modal
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
	/**
	 * initialize the max amounts when discard view shows up
	 */
	private void initMaxAmounts() {
		
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
	
	/**
	 * helper function to initialize all resource change
	 */
	private void initResourceChangeEnabled() {
		setResourceChangeEnabled(ResourceType.BRICK);
		setResourceChangeEnabled(ResourceType.ORE);
		setResourceChangeEnabled(ResourceType.SHEEP);
		setResourceChangeEnabled(ResourceType.WHEAT);
		setResourceChangeEnabled(ResourceType.WOOD);
	}
	
	private void setResourceChangeEnabled(ResourceType resource) {
		User user = cm.getModelFacade().turnManager().getUser(cm.getCurrentPlayerInfo().getId());
		boolean increase = false;
		boolean decrease = false;
		switch(resource) {
			case BRICK:
				//check that the user still has more cards for increasing, 
				//and that the total number of cards to discard is not yet reached
				increase = (user.getBrickCards() > brickToDiscard && !hasEnoughForDiscarding());
				decrease = (brickToDiscard != 0);
				break;
			case ORE:
				increase = (user.getOreCards() > oreToDiscard && !hasEnoughForDiscarding());
				decrease = (oreToDiscard != 0);
				break;
			case SHEEP:
				increase = (user.getSheepCards() > sheepToDiscard && !hasEnoughForDiscarding());
				decrease = (sheepToDiscard != 0);
				break;
			case WHEAT:
				increase = (user.getWheatCards() > wheatToDiscard && !hasEnoughForDiscarding());
				decrease = (wheatToDiscard != 0);
				break;
			case WOOD:
				increase = (user.getWoodCards() > woodToDiscard && !hasEnoughForDiscarding());
				decrease = (woodToDiscard != 0);
				break;
			default: 
				break;
		}
		getDiscardView().setResourceAmountChangeEnabled(resource, increase, decrease);
	}
	
	/**
	 * calculates the total number user has chosen to discard
	 * @return 
	 */
	private int getTotalDiscardNum() {
		
		return brickToDiscard + oreToDiscard + sheepToDiscard + wheatToDiscard + woodToDiscard;
	}
	
	/**
	 * checks that the user has total amount enough for discarding
	 * @return
	 */
	private boolean hasEnoughForDiscarding() {
		if(getTotalDiscardNum() == needToDiscard) {
			return true;
		}
		else{
			return false;
		}
	}
	
	/**
	 * checks that a user has set enough cards and can now discard
	 * @return boolean
	 */
	private void checkCanDiscard() {
		if(getTotalDiscardNum() == needToDiscard) {
			getDiscardView().setDiscardButtonEnabled(true);
		}
		else{
			getDiscardView().setDiscardButtonEnabled(false);
		}
	}
	
	/**
	 * resets the cards the users will discard after discarding
	 */
	private void resetToDiscard() {
		brickToDiscard = 0;
		oreToDiscard = 0;
		sheepToDiscard = 0;
		wheatToDiscard = 0;
		woodToDiscard = 0;
		needToDiscard = 0;
	}
	
	/**
	 * initialize the cards user plans to discard to all 0
	 */
	private void initDiscardAmount0() {
		getDiscardView().setResourceDiscardAmount(ResourceType.BRICK, 0);
		getDiscardView().setResourceDiscardAmount(ResourceType.ORE, 0);
		getDiscardView().setResourceDiscardAmount(ResourceType.SHEEP, 0);
		getDiscardView().setResourceDiscardAmount(ResourceType.WHEAT, 0);
		getDiscardView().setResourceDiscardAmount(ResourceType.WOOD, 0);
	}
	
	@Override
	public void update(Observable o, Object arg) {
		
		if(cm.getCurrentTurnPhase() == TurnPhase.DISCARDING && !isDiscarding && !ClientManager.instance().isUserRolling()) {
			System.out.println("---------------------DISCARDING---------------------");
			//boolean to keep track that user is currently discarding, or else poller keeps updating
			isDiscarding = true;
			//show modal when it's in discarding phase
			//if player has more than 8 cards, must discard
			//check that the user hasn't discarded yet
			
			int currPlayerId = cm.getCurrentPlayerInfo().getId();
//			int currPlayerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
			
			TurnManager turnManager = cm.getModelFacade().turnManager();
			
			User user = turnManager.getUser(currPlayerId);
//			User user = turnManager.getUserFromIndex(currPlayerIndex);

			int userCardCount = user.getHand().getResourceCards().getAllResourceCards().size();
			
			System.out.println("user?? current id: " + currPlayerId + " turn index: " + user.getTurnIndex() + " current player info index: " + cm.getCurrentPlayerInfo().getPlayerIndex() + " " + cm.getCurrentPlayerInfo().getName());
			
			System.out.println("user card count: " + userCardCount);
			System.out.println("has user discarded? " + user.getHasDiscarded());
			
			if(userCardCount > 7 && !user.getHasDiscarded()) {
				
				//show modal
				getDiscardView().showModal();
				
				//set all cards user plans to discard to 0
				initDiscardAmount0();
				
				//initialize the max amounts a player can discard
				initMaxAmounts();
				//get how much user has to discard and set in view
				needToDiscard = userCardCount/2;
				getDiscardView().setStateMessage("Discard: 0/" + needToDiscard);
				initResourceChangeEnabled();
				
			}
			//else show the wait view while other players discard
			else{
				getWaitView().showModal();
			}
			
		}
		//close the wait view modal if it's showing and it's not discard phase anymore
		else if(cm.getCurrentTurnPhase() != TurnPhase.DISCARDING && getWaitView().isModalShowing()) {
			getWaitView().closeModal();
		}
		
		if(cm.getCurrentTurnPhase() != TurnPhase.DISCARDING) {
			isDiscarding = false;
		}
		
	}

}

