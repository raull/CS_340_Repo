package client.maritime;

import java.util.Observable;
import java.util.Observer;

import shared.definitions.*;
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
		// TODO Auto-generated method stub
		
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

