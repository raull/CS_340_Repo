package shared.model.facade;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.PieceType;
import shared.model.Model;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.cards.Bank;
import shared.model.cards.Card;
import shared.model.cards.DevCard;
import shared.model.cards.DevCardDeck;
import shared.model.cards.Hand;
import shared.model.cards.ResourceCard;
import shared.model.game.ScoreKeeper;
import shared.model.game.TradeManager;
import shared.model.game.TurnManager;
import shared.model.game.User;
import shared.proxy.Proxy;

public class ModelFacade {
	//canDo functions
	//get pieces from models
	//will eventually have to talk to controllers
	private Proxy serverProxy; //has a pointer to the server proxy to see if methods that user has called is valid
	public Model model;
	
	/**
	 * The games's {@link ScoreKeeper} class to keep track of the score
	 */
	private ScoreKeeper score;
	/**
	 * The games's {@link TradeManager} class to keep track of all user's trading
	 */
	private TradeManager tradeManager;
	/**
	 * The games's {@link TurnManager} class to keep track of all user's turns
	 */
	private TurnManager turnManager;
	/**
	 * The games's {@link Map}
	 */
	private Map map;
	/**
	 * The games's {@link Bank} class to keep track of the game's decks
	 */
	private Bank bank;
	
	/**
	 * updates the model class with the JSON response
	 * @param jsonResponse
	 */
	public void updateModel(Model jsonResponse) {
		model.deserialize(jsonResponse);
	}
	/**
	 * gets the current model
	 * @return
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * Determines if a user can discard cards (robber)
	 * @param turnManager
	 * @param user - current user
	 * @param cards - array list of cards to be discarded
	 * @return
	 */
	public Boolean canDiscardCards(TurnManager turnManager, User user, ArrayList<Card> cards) {
		if(user != turnManager.currentUser()) return false;
		Hand currHand = user.getHand();
		for(Card card : cards) {
			if(!currHand.canRemoveCard(card)) {
				return false;
			}
		}
		return true;
	}
	
	//not really sure what this is asking
	public Boolean canRollNumber(TurnManager turnManager, User user) {
		return false;
	}
	
	/**
	 * Determines if user can buy a piece (road, settlement, city)
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @return boolean, whether user can buy a piece
	 */
	public Boolean canBuyPiece(TurnManager turnManager, User user, PieceType pieceType) {
		if(user != turnManager.currentUser() || !user.canBuyPiece(pieceType)) {
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * If user can place a road at location
	 * @param turnManager if it is user's turn
	 * @param map if the location is valid 
	 * @return
	 */
	
	public Boolean canPlaceRoadAtLoc(TurnManager turnManager, Map map, User user) {
		return null;
	}
	
	/**
	 * If user can place a building (settlement or city) at a certain location
	 * @param turnManager if it is user's turn
	 * @param map if the location is valid
	 * @return
	 */
	public Boolean canPlaceBuildingAtLoc(TurnManager turnManager, Map map) {
		return null;
	}
	
	/**
	 * If user can buy a dev card
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @param devCardDeck if there are dev cards to buy
	 * @return
	 */
	public Boolean canBuyDevCard(TurnManager turnManager, User user, DevCardDeck devCardDeck) {
		//if it's not user's turn, or dev card deck is empty, or if user canont buy dev card
		if(user != turnManager.currentUser() || devCardDeck.getAllCards().size() == 0 || !user.canBuyDevCard()) {
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * If user can play the dev card
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has the card
	 * @return
	 */
	public Boolean canPlayDevCard(TurnManager turnManager, User user, DevCard devCard) {
		if(user != turnManager.currentUser() || !user.canPlayDevCard(devCard)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * If the robber can be placed/moved to a location
	 * @param hexTile tile that the robber will be moved to
	 * @return
	 */
	public Boolean canPlaceRobber(HexTile hexTile) {
		if(hexTile.hasRobber()) {
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * If currUser can steal card from victim
	 * @param turnManager if it is currUser's turn, and if they can steal
	 * @param currUser 
	 * @param victimUser victimUser's deck, and what can be stolen
	 * @return
	 */
	public Boolean canRobPlayer(TurnManager turnManager, User currUser, User victimUser) {
		//not sure if needed
		return null;
	}
	
	/**
	 * If user can offer trade
	 * @param turnManager can only trade on user's turn
	 * @param offeredCards the cards offered 
	 * @param user if user has the card(s) offered
	 * @return
	 */
	public Boolean canOfferTrade(TurnManager turnManager, User user, ArrayList<ResourceCard> offeredCards) {
		if(user != turnManager.currentUser()) {
			return false;
		}
		ArrayList<ResourceCard> userCards  = user.getHand().getResourceCards().getAllResourceCards();
		if(!userCards.containsAll(offeredCards)) {
			return false;
		}
		return true;
	}
	
	/**
	 * If user can accept a trade
	 * @param user if user has the card(s) required to accept trade
	 * @param reqCards the cards required to accept the trade
	 * @return
	 */
	public Boolean canAcceptTrade(User user, ArrayList<ResourceCard> reqCards) {
		ArrayList<ResourceCard> userCards  = user.getHand().getResourceCards().getAllResourceCards();
		if(!userCards.containsAll(reqCards)) {
			return false;
		}
		else{
			return true;
		}	
	}
	
	/**
	 * if user can maritime trade
	 * @param bank - if bank has resources
	 * @param user - if user has resources
	 * @param map - if user has port so resources change
	 * @return
	 */
	public Boolean canMaritimeTrade(Bank bank, User user, Map map){
		return false;
	}
	
	/**
	 * If user can end the turn
	 * @param turnManager check phase -- if all done, can end
	 * @return
	 */
	public Boolean canFinishTurn(TurnManager turnManager) {
//		if(turnManager.currentTurnPhase() ) {
//			
//		}
		return null;
	}
	
	/**
	 * If user can end the game/If user has won
	 * @param turnManager user can only end game on their turn
	 * @param scoreKeeper if user has the proper score
	 * @return
	 */
	public Boolean canEndGame(TurnManager turnManager, ScoreKeeper scoreKeeper) {
		return null;
	}
	
	
	//Getters and Setters
	/**
	 * Get the {@link Bank} from the Game
	 * @return The Game's Bank
	 */
	public Bank bank() {
		return bank;
	}
	
	/**
	 * Get the {@link ScoreKeeper} from the Game
	 * @return The Game's ScoreKeeper
	 */
	public ScoreKeeper score() {
		return score;
	}
	
	/**
	 * Get the {@link TurnManager} from the Game
	 * @return The Game's TurnManager
	 */
	public TurnManager turnManager() {
		return turnManager;
	}
	
	/**
	 * Get the {@link Map} from the Game
	 * @return The Game's Map
	 */
	public Map map() {
		return map;
	}
	
	/**
	 * Get the {@link TradeManager} from the Game 
	 * @return The Game's TradeManager
	 */
	public TradeManager tradeManager() {
		return tradeManager;
	}
}
