package client.domestic;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.definitions.*;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.*;
import client.base.*;
import client.data.PlayerInfo;
import client.manager.ClientManager;
import client.misc.*;


/**
 * Domestic trade controller implementation
 */
public class DomesticTradeController extends Controller implements IDomesticTradeController, Observer {

	private IDomesticTradeOverlay tradeOverlay;
	private IWaitView waitOverlay;
	private IAcceptTradeOverlay acceptOverlay;
	private tradeResource BRICK;
	private tradeResource SHEEP;
	private tradeResource ORE;
	private tradeResource WOOD;
	private tradeResource WHEAT;
	//Index of player with whom the trade is
	private int tradePlayer;
	private boolean playersPopulated;
	private boolean tradeEnabled;
	private boolean waiting;
	private boolean accepting;
	
	
	

	/**
	 * DomesticTradeController constructor
	 * 
	 * @param tradeView Domestic trade view (i.e., view that contains the "Domestic Trade" button)
	 * @param tradeOverlay Domestic trade overlay (i.e., view that lets the user propose a domestic trade)
	 * @param waitOverlay Wait overlay used to notify the user they are waiting for another player to accept a trade
	 * @param acceptOverlay Accept trade overlay which lets the user accept or reject a proposed trade
	 */
	public DomesticTradeController(IDomesticTradeView tradeView, IDomesticTradeOverlay tradeOverlay,
									IWaitView waitOverlay, IAcceptTradeOverlay acceptOverlay) {

		super(tradeView);
		
		setTradeOverlay(tradeOverlay);
		setWaitOverlay(waitOverlay);
		setAcceptOverlay(acceptOverlay);
		ClientManager.instance().getModelFacade().addObserver(this);
		BRICK = new tradeResource();
		WHEAT = new tradeResource();
		SHEEP = new tradeResource();
		ORE = new tradeResource();
		WOOD = new tradeResource();
		playersPopulated = false;
		tradeEnabled = false;
		waiting = false;
		accepting = false;
	}
	


	public IDomesticTradeView getTradeView() {
		
		return (IDomesticTradeView)super.getView();
	}

	public IDomesticTradeOverlay getTradeOverlay() {
		return tradeOverlay;
	}

	public void setTradeOverlay(IDomesticTradeOverlay tradeOverlay) {
		this.tradeOverlay = tradeOverlay;
	}

	public IWaitView getWaitOverlay() {
		return waitOverlay;
	}

	public void setWaitOverlay(IWaitView waitView) {
		this.waitOverlay = waitView;
	}

	public IAcceptTradeOverlay getAcceptOverlay() {
		return acceptOverlay;
	}

	public void setAcceptOverlay(IAcceptTradeOverlay acceptOverlay) {
		this.acceptOverlay = acceptOverlay;
	}

	@Override
	public void startTrade() {

		//Populates the players
		if (!playersPopulated){
			List<PlayerInfo> playerList = ClientManager.instance().getCurrentGameInfo().getPlayers();
			PlayerInfo[] players = new PlayerInfo[3];
			int i = 0;
			for (PlayerInfo pi : playerList){
				if (pi.getId() != ClientManager.instance().getCurrentPlayerInfo().getId()){
					players[i] = pi;
					i++;
				}
			}
				getTradeOverlay().setPlayers(players);
				playersPopulated = true;
		}
		getTradeOverlay().showModal();
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.decrease();
			if (WOOD.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		case BRICK:
			BRICK.decrease();
			if (BRICK.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		case SHEEP:
			SHEEP.decrease();
			if (SHEEP.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		case ORE:
			ORE.decrease();
			if (ORE.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		case WHEAT:
			WHEAT.decrease();
			if (WHEAT.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		}
		updateTradeButton();
	}

	@Override
	public void increaseResourceAmount(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.increase();
			if (WOOD.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);	
		case BRICK:
			BRICK.increase();
			if (BRICK.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
		case SHEEP:
			SHEEP.increase();
			if (SHEEP.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
		case ORE:
			ORE.increase();
			if (ORE.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
		case WHEAT:
			WHEAT.increase();
			if (WHEAT.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
		}
		updateTradeButton();
		
	}

	@Override
	public void sendTradeOffer() {
		
		ResourceList offer = new ResourceList(BRICK.getNum(), ORE.getNum(), SHEEP.getNum(),
				WHEAT.getNum(), WOOD.getNum());
		
		OfferTrade trade = new OfferTrade(ClientManager.instance().getCurrentPlayerInfo()
				.getPlayerIndex(), offer, tradePlayer);
		
		JsonElement json;
		try {
			json = ClientManager.instance().getServerProxy().offerTrade(trade);
			ClientManager.instance().getModelFacade().updateModel(json);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		getTradeOverlay().closeModal();
		getWaitOverlay().showModal();
		waiting = true;
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {

		tradePlayer = playerIndex;
		updateTradeButton();
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.setNum(0);
			WOOD.setRecieve(true);
		case BRICK:
			BRICK.setNum(0);
			BRICK.setRecieve(true);
		case SHEEP:
			SHEEP.setNum(0);
			SHEEP.setRecieve(true);
		case ORE:
			ORE.setNum(0);
			ORE.setRecieve(true);
		case WHEAT:
			WHEAT.setNum(0);
			WHEAT.setRecieve(true);
		}
		
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.setNum(0);
			WOOD.setSend(true);
		case BRICK:
			BRICK.setNum(0);
			BRICK.setSend(true);
		case SHEEP:
			SHEEP.setNum(0);
			SHEEP.setSend(true);
		case ORE:
			ORE.setNum(0);
			ORE.setSend(true);
		case WHEAT:
			WHEAT.setNum(0);
			WHEAT.setSend(true);
		}
		if (!canIncrease(resource))
			getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
	}

	@Override
	public void unsetResource(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.setNum(0);
		case BRICK:
			BRICK.setNum(0);
		case SHEEP:
			SHEEP.setNum(0);
		case ORE:
			ORE.setNum(0);
		case WHEAT:
			WHEAT.setNum(0);
		}
	}

	//Checks to see if the trade has at least one resource being sent
	public boolean haveSend(){
		boolean hasSend = false;
		if (WOOD.isSend && WOOD.getNum() > 0)
			hasSend = true;
		if (BRICK.isSend && BRICK.getNum() > 0)
			hasSend = true;
		if (SHEEP.isSend && SHEEP.getNum() > 0)
			hasSend = true;
		if (WHEAT.isSend && WHEAT.getNum() > 0)
			hasSend = true;
		if (ORE.isSend && ORE.getNum() > 0)
			hasSend = true;
		
		return hasSend;
	}
	
	//Checks to see if the trade has at least one resource to be received
	public boolean haveRecieve(){
		boolean hasRecieve = false;
		if (WOOD.isRecieve && WOOD.getNum() < 0)
			hasRecieve = true;
		if (BRICK.isRecieve && BRICK.getNum() < 0)
			hasRecieve = true;
		if (SHEEP.isRecieve && SHEEP.getNum() < 0)
			hasRecieve = true;
		if (WHEAT.isRecieve && WHEAT.getNum() < 0)
			hasRecieve = true;
		if (ORE.isRecieve && ORE.getNum() < 0)
			hasRecieve = true;
		return hasRecieve;
	}
	
	//Updates the Trade Button
	public void updateTradeButton(){
		if (haveSend() && haveRecieve() && tradePlayer == -1){
			getTradeOverlay().setStateMessage("Select with whom you will trade");
			getTradeOverlay().setPlayerSelectionEnabled(true);
			getTradeOverlay().setTradeEnabled(false);
		}
		else if (haveSend() && haveRecieve() && tradePlayer != -1){
			getTradeOverlay().setStateMessage("Trade!");
			getTradeOverlay().setPlayerSelectionEnabled(true);
			getTradeOverlay().setTradeEnabled(true);
		}
		else {
			getTradeOverlay().setStateMessage("Select the trade you want to make");
			getTradeOverlay().setTradeEnabled(false);
		}
		
	}
	@Override
	public void cancelTrade() {
		//Resets all values
		BRICK = new tradeResource();
		WHEAT = new tradeResource();
		SHEEP = new tradeResource();
		ORE = new tradeResource();
		WOOD = new tradeResource();
		
		getTradeOverlay().closeModal();
	}

	@Override
	public void acceptTrade(boolean willAccept) {
		AcceptTrade accept = new AcceptTrade(ClientManager.instance().getCurrentPlayerInfo()
				.getPlayerIndex(), willAccept);
		
		try {
			JsonElement json = ClientManager.instance().getServerProxy().acceptTrade(accept);
			ClientManager.instance().getModelFacade().updateModel(json);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getAcceptOverlay().closeModal();
		accepting = false;
	}

	//Checks to see if the user can send more of the resource
	public boolean canIncrease(ResourceType resource){
		int inHand;
		boolean canInc = false;
		inHand = ClientManager.instance().getModelFacade().turnManager().currentUser()
				.getHand().getResourceCards().getCountByType(resource);
		switch (resource){
		case WOOD:
			canInc =  WOOD.getNum() < inHand;
		case BRICK:
			canInc = BRICK.getNum() < inHand;
		case ORE:
			canInc = ORE.getNum() < inHand;
		case SHEEP:
			canInc = SHEEP.getNum() < inHand;
		case WHEAT:
			canInc = WHEAT.getNum() < inHand;
		}
		return canInc;
	}
	@Override
	public void update(Observable arg0, Object arg1) {
		if (ClientManager.instance().hasGameStarted()){
		//Enables Domestic Trade Option if necessary
		if (ClientManager.instance().getModelFacade().turnManager().getCurrentTurn() ==
				ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex() &&
				ClientManager.instance().getCurrentTurnPhase().name().equals("PLAYING")){
			if (!tradeEnabled){
				getTradeView().enableDomesticTrade(true);
				tradeEnabled = true;
			}
		}
		else {
			if (tradeEnabled){
				getTradeView().enableDomesticTrade(false);
				tradeEnabled = false;
			}
		}
		// Shows the Accept Overlay if necessary
		if (ClientManager.instance().getModelFacade().getModel().getTradeOffer() != null
				&& ClientManager.instance().getModelFacade().getModel().getTradeOffer().getReceiverIndex() 
				== ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex()){
			if (!accepting){
				getAcceptOverlay().showModal();
				accepting = true;
			}
		}
		// Removes Wait Overlay if Trade is accepted or rejected
		if (ClientManager.instance().getModelFacade().getModel().getTradeOffer() == null){
			if (waiting){
				getWaitOverlay().closeModal();
				waiting = false;
			}
		}
		// Sets the boolean for the Accept Overlay
		if (waiting)
			getAcceptOverlay().setAcceptEnabled(canAcceptIt());
		}
	}
	// Checks to see if player can accept the trade	
	public boolean canAcceptIt(){
		TurnManager turnMan = ClientManager.instance().getModelFacade().turnManager();
		User curUser = turnMan.currentUser();
		
		return ClientManager.instance().getModelFacade().canAcceptTrade(turnMan, curUser, 
				ClientManager.instance().getModelFacade().getModel().getTradeOffer());
		}

	// Class used for tracking how much of a resource is sent or is received
	private class tradeResource {
		private int num;
		private boolean isSend;
		private boolean isRecieve;
		public int getNum() {
			int amount = 0;
			if (isSend)
				return num;
			else if (isRecieve)
				return -num;
			return amount;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public boolean isSend() {
			return isSend;
		}
		public void setSend(boolean isSend) {
			this.isSend = isSend;
			if (isSend)
				isRecieve = false;
		}
		
		public boolean isRecieve(){
			return isRecieve();
		}
		public void setRecieve(boolean recieve){
			this.isRecieve = recieve;
			if (isRecieve){
				isSend = false;
			}
		}
		
		public tradeResource(){
			setNum(0);
			setSend(false);
			setRecieve(false);
		}
		
		
		public void increase(){
			num++;
		}
		
		public void decrease(){
			num--;
		}
	}
	
	}


