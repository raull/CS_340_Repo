package client.maritime;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.definitions.*;
import shared.model.board.Port;
import shared.model.cards.Bank;
import shared.model.cards.ResourceCard;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.MaritimeTrade;
import client.base.*;
import client.manager.ClientManager;
import client.misc.MessageView;


/**
 * Implementation for the maritime trade controller
 */
public class MaritimeTradeController extends Controller implements IMaritimeTradeController, Observer {

	private IMaritimeTradeOverlay tradeOverlay;
	private ResourceType outResource;
	private ResourceType inResource;
	
	private ClientManager cm = ClientManager.instance();
	
	private int brickRatio = 4;
	private int oreRatio = 4;
	private int sheepRatio = 4;
	private int wheatRatio = 4;
	private int woodRatio = 4;
	
	private List<ResourceType> givingResourceTypes = new ArrayList<ResourceType>();
	private List<ResourceType> gettingResourceTypes = new ArrayList<ResourceType>();
	
	
	public MaritimeTradeController(IMaritimeTradeView tradeView, IMaritimeTradeOverlay tradeOverlay) {
		
		super(tradeView);

		setTradeOverlay(tradeOverlay);
		ClientManager.instance().getModelFacade().addObserver(this);
	}
	
	public IMaritimeTradeView getTradeView() {
		
		return (IMaritimeTradeView)super.getView();
	}
	
	public IMaritimeTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IMaritimeTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	@Override
	public void startTrade() {
		
		//show the overlay
		getTradeOverlay().reset();
		getTradeOverlay().showModal();
		initGiveOptions();
		getTradeOverlay().setTradeEnabled(false);
		//initGetOptions();
	}

	@Override
	public void makeTrade() {
		
		int playerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
		int ratio = getRatio(inResource); //change after checking for ports
		String outputResource = inResource.toString().toLowerCase();
		String inputResource = outResource.toString().toLowerCase();

		try {
			
			MaritimeTrade tradeMaritimeReq = new MaritimeTrade(playerIndex, ratio, outputResource, inputResource);
			//get proxy to maritime trade
			cm.getServerProxy().maritimeTrade(tradeMaritimeReq);
			cm.forceUpdate();
			//close overlay after trade
			getTradeOverlay().closeModal();
			getTradeOverlay().reset();
			gettingResourceTypes.clear();
			givingResourceTypes.clear();
		} catch (ProxyException e) {
			e.printStackTrace();
			MessageView alertView = new MessageView();
			alertView.setTitle("Error");
			alertView.setMessage(e.getLocalizedMessage());
			alertView.showModal();
		}
		
	}

	@Override
	public void cancelTrade() {

		getTradeOverlay().closeModal();
	}

	@Override
	public void setGetResource(ResourceType resource) {
		//sets the resource user is getting
		getTradeOverlay().selectGetOption(resource, 1);
		inResource = resource;
		getTradeOverlay().setTradeEnabled(true);
	}

	@Override
	public void setGiveResource(ResourceType resource) {
		//sets the resource user is giving
		int ratio = getRatio(resource);
		getTradeOverlay().selectGiveOption(resource, ratio);
		initGetOptions();
		outResource = resource;
	}

	@Override
	public void unsetGetValue() {
		//reset the chosen get resource
		initGetOptions();
		inResource = null;
	}

	@Override
	public void unsetGiveValue() {
		//reset the chosen give resource
		initGiveOptions();
		outResource = null;
	}

	@Override
	public void update(Observable o, Object arg) {
		TurnManager tm = cm.instance().getModelFacade().turnManager();
		int playerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
		// enable maritime trade during playing phase
		if(cm.getCurrentTurnPhase() == TurnPhase.PLAYING && tm.getCurrentTurn() ==
				playerIndex) {
			//update what ratios players can trade at based on ports
			udpateResourceRatio();
			getTradeView().enableMaritimeTrade(true);
		}
		else{
			getTradeView().enableMaritimeTrade(false);
		}
		gettingResourceTypes.clear();
		givingResourceTypes.clear();
	}
	
	/**
	 * helper function to help update the ratios if user has a THREE port
	 */
	private void updateResourceRatioTHREE() {
		//if default ratio of 4, change to 3
		//if ratio is less than 3, ignore
		if(brickRatio == 4) {
			brickRatio = 3;
		}
		if(oreRatio == 4) {
			oreRatio = 3;
		}
		if(sheepRatio == 4) {
			sheepRatio = 3;
		}
		if(wheatRatio == 4) {
			wheatRatio = 3;
		}
		if(woodRatio == 4) {
			woodRatio = 3;
		}
		
	}
	
	//returns the current ratio of the resource type
	public int getRatio(ResourceType resource){
		int ratio = 0;
		switch (resource){
		case BRICK:
			ratio = brickRatio;
			break;
		case WOOD:
			ratio = woodRatio;
			break;
		case ORE:
			ratio = oreRatio;
			break;
		case SHEEP:
			ratio = sheepRatio;
			break;
		case WHEAT:
			ratio = wheatRatio;
			break;
		}
		
		return ratio;
	}
	
	/**
	 * checks what ports user has, and change ratio of resources accordingly
	 * @param resource
	 */
	private void udpateResourceRatio() {
		//go through user ports and call getPortResourceType on each port
		int playerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
		User currUser = cm.getModelFacade().turnManager().getUser(playerIndex);
		
		for(Port port : currUser.ports()) {
			switch(port.getType()) {
				case BRICK:
					brickRatio = 2;
					break;
				case ORE:
					oreRatio = 2;
					break;
				case SHEEP:
					sheepRatio = 2;
					break;
				case WHEAT:
					wheatRatio = 2;
					break;
				case WOOD:
					woodRatio = 2;
					break;
				case THREE:
					updateResourceRatioTHREE();
					break;
			}
		}
	}
	
	/**
	 * checks what cards user has
	 */
	private void checkUserCards(){
		//go through cards user has
		// if user has cards greater than ratio, allow user to give that resource
		int playerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
		User currUser = cm.getModelFacade().turnManager().getUser(playerIndex);
		
		if(currUser.getBrickCards() >= brickRatio) {
			givingResourceTypes.add(ResourceType.BRICK);
		}
		if(currUser.getOreCards() >= oreRatio) {
			givingResourceTypes.add(ResourceType.ORE);
		}
		if(currUser.getSheepCards() >= sheepRatio) {
			givingResourceTypes.add(ResourceType.SHEEP);
		}
		if(currUser.getWheatCards() >= wheatRatio) {
			givingResourceTypes.add(ResourceType.WHEAT);
		}
		if(currUser.getWoodCards() >= woodRatio) {
			givingResourceTypes.add(ResourceType.WOOD);
		}
		
	}
	
	/**
	 * check that the bank has enough resources (almost never reach?)
	 */
	private void checkBankCards() {
		Bank bank = cm.getModelFacade().bank();
		
		//check that the card type exists in bank and enable
		if(bank.canDrawResourceCard(new ResourceCard(ResourceType.BRICK))) {
			gettingResourceTypes.add(ResourceType.BRICK);
		}
		if(bank.canDrawResourceCard(new ResourceCard(ResourceType.ORE))) {
			gettingResourceTypes.add(ResourceType.ORE);
		}
		if(bank.canDrawResourceCard(new ResourceCard(ResourceType.SHEEP))) {
			gettingResourceTypes.add(ResourceType.SHEEP);
		}
		if(bank.canDrawResourceCard(new ResourceCard(ResourceType.WHEAT))) {
			gettingResourceTypes.add(ResourceType.WHEAT);
		}
		if(bank.canDrawResourceCard(new ResourceCard(ResourceType.WOOD))) {
			gettingResourceTypes.add(ResourceType.WOOD);
		}

	}
	
	/**
	 * initializes the resource types that the user can give
	 * ratio is 4 by default, unless user has ports
	 */
	private void initGiveOptions() {
		//check what resources user has
		checkUserCards();
		//show ones users have
		ResourceType[] enabledResources = new ResourceType[givingResourceTypes.size()];
		givingResourceTypes.toArray(enabledResources);
		getTradeOverlay().showGiveOptions(enabledResources);
	}
	
	/**
	 * initializes the resource types that the user can receive
	 * based on what the bank has available
	 */
	private void initGetOptions() {
		//check what resources bank has
		checkBankCards();
		//show the enabled ones
		ResourceType[] enabledResources = new ResourceType[gettingResourceTypes.size()];
		gettingResourceTypes.toArray(enabledResources);
		getTradeOverlay().showGetOptions(enabledResources);

	}

}

