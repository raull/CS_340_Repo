package client.devcards;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.cards.DevCard;
import shared.proxy.ProxyException;
import shared.proxy.moves.*;
import client.base.*;
import client.manager.ClientManager;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController, Observer {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	private boolean isBuy;
	private boolean isPlay;
	
	/**
	 * DevCardController constructor
	 * 
	 * @param view "Play dev card" view
	 * @param buyCardView "Buy dev card" view
	 * @param soldierAction Action to be executed when the user plays a soldier card.  It calls "mapController.playSoldierCard()".
	 * @param roadAction Action to be executed when the user plays a road building card.  It calls "mapController.playRoadBuildingCard()".
	 */
	public DevCardController(IPlayDevCardView view, IBuyDevCardView buyCardView, 
								IAction soldierAction, IAction roadAction) {

		super(view);
		
		this.buyCardView = buyCardView;
		this.soldierAction = soldierAction;
		this.roadAction = roadAction;
		isBuy = false;
		isPlay = false;
		ClientManager.instance().getModelFacade().addObserver(this);
	}

	public IPlayDevCardView getPlayCardView() {
		return (IPlayDevCardView)super.getView();
	}

	public IBuyDevCardView getBuyCardView() {
		return buyCardView;
	}

	@Override
	public void startBuyCard() {
		
		getBuyCardView().showModal();
		isBuy = true;
	}

	@Override
	public void cancelBuyCard() {
		
		getBuyCardView().closeModal();
		isBuy = false;
	}

	@Override
	public void buyCard() {
		BuyDevCard buyDev = new BuyDevCard(
				ClientManager.instance().getModelFacade().
				turnManager().currentUser().getTurnIndex());
		try {
			JsonElement json = ClientManager.instance().getServerProxy().buyDevCard(buyDev);
			ClientManager.instance().getModelFacade().updateModel(json);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getBuyCardView().closeModal();
		isBuy = false;
	}

	@Override
	public void startPlayCard() {
		
		getPlayCardView().showModal();
		isPlay = true;
	}

	@Override
	public void cancelPlayCard() {

		getPlayCardView().closeModal();
		isPlay = false;
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		Monopoly_ playMonopoly = new Monopoly_(resource, getPlayerIndex());
		
		try {
			JsonElement json = ClientManager.instance().getServerProxy().Monopoly(playMonopoly);
			ClientManager.instance().getModelFacade().updateModel(json);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cancelPlayCard();
	}

	@Override
	public void playMonumentCard() {
		Monument_ playMonument = new Monument_(getPlayerIndex());
		
		try {
			JsonElement json = ClientManager.instance().getServerProxy().Monument(playMonument);
			ClientManager.instance().getModelFacade().updateModel(json);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cancelPlayCard();
	}

	@Override
	public void playRoadBuildCard() {
		isPlay = false;
		roadAction.execute();
	}

	@Override
	public void playSoldierCard() {
		isPlay = false;
		soldierAction.execute();
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		Year_of_Plenty_ playYOP = new Year_of_Plenty_(getPlayerIndex(), resource1, resource2);
		
		try {
			JsonElement json = ClientManager.instance().getServerProxy().Year_of_Plenty(playYOP);
			ClientManager.instance().getModelFacade().updateModel(json);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cancelPlayCard();
	}

	@Override
	public void update(Observable o, Object arg) {
		setEnabledCard(DevCardType.SOLDIER);
		setEnabledCard(DevCardType.MONOPOLY);
		setEnabledCard(DevCardType.MONUMENT);
		setEnabledCard(DevCardType.ROAD_BUILD);
		setEnabledCard(DevCardType.YEAR_OF_PLENTY);
		
		setCardAmount(DevCardType.MONOPOLY);
		setCardAmount(DevCardType.MONUMENT);
		setCardAmount(DevCardType.ROAD_BUILD);
		setCardAmount(DevCardType.SOLDIER);
		setCardAmount(DevCardType.YEAR_OF_PLENTY);
		if (isBuy && !getBuyCardView().isModalShowing()){
			getBuyCardView().showModal();
		}
		if (isPlay && !getPlayCardView().isModalShowing()){
			getPlayCardView().showModal();
		}
	}

	//Checks to see whether player can play card or not, and sets the View accordingly
	public void setEnabledCard(DevCardType devType){
		getPlayCardView().setCardEnabled(devType, 
				ClientManager.instance().getModelFacade().turnManager().
				currentUser().canPlayDevCard(new DevCard(devType)));
	}
	
	//Sets how many total development cards of each type are in user's hand
	public void setCardAmount(DevCardType devType){
		int oldCards;
		int newCards;
		oldCards = ClientManager.instance().getModelFacade().turnManager().currentUser()
				.getHand().getUsableDevCards().getCountByType(devType);
		newCards = ClientManager.instance().getModelFacade().turnManager().currentUser()
				.getHand().getNewDevCards().getCountByType(devType);
		
		int totalCards = oldCards + newCards;
		
		getPlayCardView().setCardAmount(devType, totalCards);
	}
	
	// Returns the player Index of the client
	public int getPlayerIndex(){
		return ClientManager.instance().getModelFacade().
				turnManager().currentUser().getTurnIndex();
	}
}

