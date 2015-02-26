package client.domestic;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

import shared.definitions.*;
import shared.model.game.TurnManager;
import shared.model.game.User;
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
		case BRICK:
			BRICK.decrease();
		case SHEEP:
			SHEEP.decrease();
		case ORE:
			ORE.decrease();
		case WHEAT:
			WHEAT.decrease();
		}
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
		
		
	}

	@Override
	public void sendTradeOffer() {
		
		ResourceList offer = new ResourceList(BRICK.getNum(), ORE.getNum(), SHEEP.getNum(),
				WHEAT.getNum(), WOOD.getNum());
		
		getTradeOverlay().closeModal();
		getWaitOverlay().showModal();
		waiting = true;
	}

	@Override
	public void setPlayerToTradeWith(int playerIndex) {

		tradePlayer = playerIndex;
	}

	@Override
	public void setResourceToReceive(ResourceType resource) {
		switch (resource){
		case WOOD:
			WOOD.setNum(0);
			WOOD.setSend(false);
		case BRICK:
			BRICK.setNum(0);
			BRICK.setSend(false);
		case SHEEP:
			SHEEP.setNum(0);
			SHEEP.setSend(false);
		case ORE:
			ORE.setNum(0);
			ORE.setSend(false);
		case WHEAT:
			WHEAT.setNum(0);
			WHEAT.setSend(false);
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
		public int getNum() {
			if (isSend)
				return num;
			else
				return -num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public boolean isSend() {
			return isSend;
		}
		public void setSend(boolean isSend) {
			this.isSend = isSend;
		}
		
		public tradeResource(){
			setNum(0);
			setSend(false);
		}
		
		
		public void increase(){
			num++;
		}
		
		public void decrease(){
			num--;
		}
	}
	
	}


