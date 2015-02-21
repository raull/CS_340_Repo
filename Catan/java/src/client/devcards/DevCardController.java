package client.devcards;

import java.util.Observable;
import java.util.Observer;

import com.google.gson.JsonElement;

import shared.definitions.DevCardType;
import shared.definitions.ResourceType;
import shared.model.cards.DevCard;
import shared.proxy.ProxyException;
import shared.proxy.moves.BuyDevCard;
import client.base.*;
import client.manager.ClientManager;


/**
 * "Dev card" controller implementation
 */
public class DevCardController extends Controller implements IDevCardController, Observer {

	private IBuyDevCardView buyCardView;
	private IAction soldierAction;
	private IAction roadAction;
	
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
	}

	@Override
	public void cancelBuyCard() {
		
		getBuyCardView().closeModal();
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
	}

	@Override
	public void startPlayCard() {
		
		getPlayCardView().showModal();
	}

	@Override
	public void cancelPlayCard() {

		getPlayCardView().closeModal();
	}

	@Override
	public void playMonopolyCard(ResourceType resource) {
		
	}

	@Override
	public void playMonumentCard() {
		
	}

	@Override
	public void playRoadBuildCard() {
		
		roadAction.execute();
	}

	@Override
	public void playSoldierCard() {
		
		soldierAction.execute();
	}

	@Override
	public void playYearOfPlentyCard(ResourceType resource1, ResourceType resource2) {
		
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
}

