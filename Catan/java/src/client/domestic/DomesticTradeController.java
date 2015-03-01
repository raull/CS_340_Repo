package client.domestic;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.definitions.*;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.TradeOffer;
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
	private boolean acceptSet;
	private boolean trading;
	
	
	

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
		trading = false;
		
		waiting = false;
		accepting = false;
		acceptSet = false;
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
			List<User> playerList = ClientManager.instance().getModelFacade().turnManager().getUsers();
			PlayerInfo[] players = new PlayerInfo[3];
			int i = 0;
			for (User pi : playerList){
				if (pi.getPlayerID() != ClientManager.instance().getCurrentPlayerInfo().getId()){
					players[i] = pi.getPlayerInfo();
					i++;
				}
			}
				getTradeOverlay().setPlayers(players);
				playersPopulated = true;
		}
		getTradeOverlay().showModal();
		trading = true;
	}

	@Override
	public void decreaseResourceAmount(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.decrease();
			if (WOOD.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
			break;
		case BRICK:
			BRICK.decrease();
			if (BRICK.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
			break;
		case SHEEP:
			SHEEP.decrease();
			if (SHEEP.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
			break;
		case ORE:
			ORE.decrease();
			if (ORE.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
			break;
		case WHEAT:
			WHEAT.decrease();
			if (WHEAT.isSend && canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
			break;
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
			break;
		case BRICK:
			BRICK.increase();
			if (BRICK.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
			break;
		case SHEEP:
			SHEEP.increase();
			if (SHEEP.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
			break;
		case ORE:
			ORE.increase();
			if (ORE.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
			break;
		case WHEAT:
			WHEAT.increase();
			if (WHEAT.isSend && !canIncrease(resource))
				getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
			break;
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
		trading = false;
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
			break;
		case BRICK:
			BRICK.setNum(0);
			BRICK.setRecieve(true);
			break;
		case SHEEP:
			SHEEP.setNum(0);
			SHEEP.setRecieve(true);
			break;
		case ORE:
			ORE.setNum(0);
			ORE.setRecieve(true);
			break;
		case WHEAT:
			WHEAT.setNum(0);
			WHEAT.setRecieve(true);
			break;
		}
		getTradeOverlay().setResourceAmountChangeEnabled(resource, true, true);
		updateTradeButton();
	}

	@Override
	public void setResourceToSend(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.setNum(0);
			WOOD.setSend(true);
			break;
		case BRICK:
			BRICK.setNum(0);
			BRICK.setSend(true);
			break;
		case SHEEP:
			SHEEP.setNum(0);
			SHEEP.setSend(true);
			break;
		case ORE:
			ORE.setNum(0);
			ORE.setSend(true);
			break;
		case WHEAT:
			WHEAT.setNum(0);
			WHEAT.setSend(true);
			break;
		}
		if (!canIncrease(resource))
			getTradeOverlay().setResourceAmountChangeEnabled(resource, false, true);
		updateTradeButton();
	}

	@Override
	public void unsetResource(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.setNum(0);
			break;
		case BRICK:
			BRICK.setNum(0);
			break;
		case SHEEP:
			SHEEP.setNum(0);
			break;
		case ORE:
			ORE.setNum(0);
			break;
		case WHEAT:
			WHEAT.setNum(0);
			break;
		}
		updateTradeButton();
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
		trading = false;
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
		acceptSet = false;
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
			break;
		case BRICK:
			canInc = BRICK.getNum() < inHand;
			break;
		case ORE:
			canInc = ORE.getNum() < inHand;
			break;
		case SHEEP:
			canInc = SHEEP.getNum() < inHand;
			break;
		case WHEAT:
			canInc = WHEAT.getNum() < inHand;
			break;
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
				getTradeView().enableDomesticTrade(false);
				tradeEnabled = false;
			
		}
		
		//If trading, keeps modal up
		if (trading && !getTradeOverlay().isModalShowing()){
			getTradeOverlay().showModal();
		}
		// Shows the Accept Overlay if necessary
		if (ClientManager.instance().getModelFacade().getModel().getTradeOffer() != null
				&& ClientManager.instance().getModelFacade().getModel().getTradeOffer().getReceiverIndex() 
				== ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex()){
		//	if (!accepting){
				if (!acceptSet){
					setAcceptWindow();
					acceptSet = true;
				}
				if (!getAcceptOverlay().isModalShowing())
				getAcceptOverlay().showModal();
				accepting = true;
		//	}
		}
		// Removes Wait Overlay if Trade is accepted or rejected
		if (ClientManager.instance().getModelFacade().getModel().getTradeOffer() == null){
			if (waiting){
				getWaitOverlay().closeModal();
				waiting = false;
				
				BRICK = new tradeResource();
				WHEAT = new tradeResource();
				SHEEP = new tradeResource();
				ORE = new tradeResource();
				WOOD = new tradeResource();
				//cancelTrade();
			}
		}
		}
		// Sets the boolean for the Accept Overlay
		
	}
	// Checks to see if player can accept the trade	
	public boolean canAcceptIt(){
		TurnManager turnMan = ClientManager.instance().getModelFacade().turnManager();
		User curUser = turnMan.getUserFromIndex(ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex());
		
		return ClientManager.instance().getModelFacade().canAcceptTrade(turnMan, curUser, 
				ClientManager.instance().getModelFacade().getModel().getTradeOffer());
		}

	
	// Sets the Accept Window
	public void setAcceptWindow(){
		TradeOffer trade = ClientManager.instance().getModelFacade().getModel().getTradeOffer();
		
		//Clears any previous info
		getAcceptOverlay().reset();
		//Populates the offered resources
		ResourceCardDeck sendDeck = trade.getSendingDeck();
		
		int bricks = sendDeck.getCountByType(ResourceType.BRICK);
		int ores = sendDeck.getCountByType(ResourceType.ORE);
		int wheats = sendDeck.getCountByType(ResourceType.WHEAT);
		int sheeps = sendDeck.getCountByType(ResourceType.SHEEP);
		int woods = sendDeck.getCountByType(ResourceType.WOOD);
		
		if (bricks > 0)
			getAcceptOverlay().addGetResource(ResourceType.BRICK, bricks);
		if (ores > 0)
			getAcceptOverlay().addGetResource(ResourceType.ORE, ores);
		if (wheats > 0)
			getAcceptOverlay().addGetResource(ResourceType.WHEAT, wheats);
		if (sheeps > 0)
			getAcceptOverlay().addGetResource(ResourceType.SHEEP, sheeps);
		if (woods > 0)
			getAcceptOverlay().addGetResource(ResourceType.WOOD, woods);
		
		//Populates the requested resources
		ResourceCardDeck recDeck = trade.getReceivingDeck();
		
		int brickr = recDeck.getCountByType(ResourceType.BRICK);
		int orer = recDeck.getCountByType(ResourceType.ORE);
		int wheatr = recDeck.getCountByType(ResourceType.WHEAT);
		int sheepr = recDeck.getCountByType(ResourceType.SHEEP);
		int woodr = recDeck.getCountByType(ResourceType.WOOD);
		
		if (brickr > 0)
			getAcceptOverlay().addGiveResource(ResourceType.BRICK, brickr);
		if (orer > 0)
			getAcceptOverlay().addGiveResource(ResourceType.ORE, orer);
		if (wheatr > 0)
			getAcceptOverlay().addGiveResource(ResourceType.WHEAT, wheatr);
		if (sheepr > 0)
			getAcceptOverlay().addGiveResource(ResourceType.SHEEP, sheepr);
		if (woodr > 0)
			getAcceptOverlay().addGiveResource(ResourceType.WHEAT, woodr);
		
		//Gets the sender's name
		User sendingUser = ClientManager.instance().getModelFacade().turnManager()
				.getUserFromIndex(trade.getSenderIndex());
		
		String sendingName = sendingUser.getName();
		
		getAcceptOverlay().setPlayerName(sendingName);
		// Enables the accept button
		getAcceptOverlay().setAcceptEnabled(canAcceptIt());
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


