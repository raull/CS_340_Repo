package client.resources;

import java.util.*;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.cards.DevCardDeck;
import shared.model.cards.ResourceCardDeck;
import shared.model.facade.ModelFacade;
import shared.model.game.User;
import client.base.*;
import client.data.PlayerInfo;
import client.manager.ClientManager;


/**
 * Implementation for the resource bar controller
 */
public class ResourceBarController extends Controller implements IResourceBarController, Observer {

	private Map<ResourceBarElement, IAction> elementActions;
	
	public ResourceBarController(IResourceBarView view) {

		super(view);
		
		elementActions = new HashMap<ResourceBarElement, IAction>();
		ClientManager.instance().getModelFacade().addObserver(this);
	}

	@Override
	public IResourceBarView getView() {
		return (IResourceBarView)super.getView();
	}

	/**
	 * Sets the action to be executed when the specified resource bar element is clicked by the user
	 * 
	 * @param element The resource bar element with which the action is associated
	 * @param action The action to be executed
	 */
	public void setElementAction(ResourceBarElement element, IAction action) {

		elementActions.put(element, action);
	}

	@Override
	public void buildRoad() {
		executeElementAction(ResourceBarElement.ROAD);
	}

	@Override
	public void buildSettlement() {
		executeElementAction(ResourceBarElement.SETTLEMENT);
	}

	@Override
	public void buildCity() {
		executeElementAction(ResourceBarElement.CITY);
	}

	@Override
	public void buyCard() {
		executeElementAction(ResourceBarElement.BUY_CARD);
	}

	@Override
	public void playCard() {
		executeElementAction(ResourceBarElement.PLAY_CARD);
	}
	
	private void executeElementAction(ResourceBarElement element) {
		
		if (elementActions.containsKey(element)) {
			
			IAction action = elementActions.get(element);
			action.execute();
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		
		//Get the info
		ClientManager cm = ClientManager.instance();
		PlayerInfo player = cm.getCurrentPlayerInfo();
		ModelFacade facade = cm.getModelFacade();
		
		int currentIndex = player.getPlayerIndex();
		
		User currentUser = facade.turnManager().getUserFromIndex(currentIndex);
		ResourceCardDeck resourceHand = currentUser.getHand().getResourceCards();
		DevCardDeck devHand = currentUser.getHand().getUsableDevCards();
		
		//Update view
		getView().setElementAmount(ResourceBarElement.WOOD, resourceHand.getCountByType(ResourceType.WOOD));
		getView().setElementAmount(ResourceBarElement.BRICK, resourceHand.getCountByType(ResourceType.BRICK));
		getView().setElementAmount(ResourceBarElement.SHEEP, resourceHand.getCountByType(ResourceType.SHEEP));
		getView().setElementAmount(ResourceBarElement.WHEAT, resourceHand.getCountByType(ResourceType.WHEAT));
		getView().setElementAmount(ResourceBarElement.ORE, resourceHand.getCountByType(ResourceType.ORE));

		getView().setElementAmount(ResourceBarElement.ROAD, currentUser.getUnusedRoads());
		getView().setElementAmount(ResourceBarElement.SOLDIERS, devHand.getCountByType(DevCardType.SOLDIER));
		getView().setElementAmount(ResourceBarElement.SETTLEMENT, currentUser.getUnusedSettlements());
		getView().setElementAmount(ResourceBarElement.CITY, currentUser.getUnusedCities());
		
	}

}

