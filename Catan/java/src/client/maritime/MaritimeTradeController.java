package client.maritime;

import java.util.Observable;
import java.util.Observer;

import shared.definitions.*;
import shared.model.board.Port;
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
		getTradeOverlay().showModal();
	}

	@Override
	public void makeTrade() {
		
		int playerIndex = cm.getCurrentPlayerInfo().getPlayerIndex();
		int ratio = 4; //change after checking for ports
		String outputResource = outResource.toString();
		String inputResource = inResource.toString();

		try {
			
			MaritimeTrade tradeMaritimeReq = new MaritimeTrade(playerIndex, ratio, outputResource, inputResource);
			//get proxy to maritime trade
			cm.getServerProxy().maritimeTrade(tradeMaritimeReq);
			//close overlay after trade
			getTradeOverlay().closeModal();
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
		inResource = resource;
	}

	@Override
	public void setGiveResource(ResourceType resource) {
		//sets the resource user is giving
		outResource = resource;
	}

	@Override
	public void unsetGetValue() {
		//reset the chosen get resource
		inResource = null;
	}

	@Override
	public void unsetGiveValue() {
		//reset the chosen give resource
		outResource = null;
	}

	@Override
	public void update(Observable o, Object arg) {
		// enable maritime trade during playing phase
		if(cm.getCurrentTurnPhase() == TurnPhase.PLAYING) {
			getTradeView().enableMaritimeTrade(true);
		}
		else{
			getTradeView().enableMaritimeTrade(false);
		}
		
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
		
		
	}
	
	/**
	 * check that the bank has enough resources (almost never reach?)
	 */
	private void checkBankCards() {
		
	}
	
	/**
	 * initializes the resource types that the user can give
	 * ratio is 4 by default, unless user has ports
	 */
	private void initGiveOptions() {
		//call the following function from overlay
		//showGiveOptions(ResourceType[] enabledResources)
	}
	
	/**
	 * initializes the resource types that the user can receive
	 * based on what the bank has available
	 */
	private void initGetOptions() {
		
	}

}

