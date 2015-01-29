package shared.model.facade;

import com.google.gson.JsonObject;

import shared.model.Model;
import shared.model.board.Map;
import shared.model.cards.Bank;
import shared.model.cards.DevCardDeck;
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
	public void updateModel(JsonObject jsonResponse) {
		
	}
	
	/**
	 * Determines if user can buy a road
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @return boolean, whether user can buy road
	 */
	public Boolean canBuyRoad(TurnManager turnManager, User user) {
		return null;
	}
	
	/**
	 * If user can place a road at location
	 * @param turnManager if it is user's turn
	 * @param map if the location is valid 
	 * @return
	 */
	
	public Boolean canPlaceRoadAtLoc(TurnManager turnManager, Map map) {
		return null;
	}
	
	/**
	 * If user can buy a building (settlement or city)
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @return
	 */
	public Boolean canBuyBuilding(TurnManager turnManager, User user) {
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
		return null;
	}
	
	/**
	 * If user can play the dev card
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has the card
	 * @return
	 */
	public Boolean canPlayDevCard(TurnManager turnManager, User user) {
		return null;
	}
	
	/**
	 * If the robber can be placed/moved
	 * @param turnManager check if dice rolled is 7
	 * @return
	 */
	public Boolean canPlaceRobber(TurnManager turnManager) {
		return null;
	}
	
	/**
	 * If currUser can steal card from victim
	 * @param turnManager if it is currUser's turn, and if they can steal
	 * @param currUser 
	 * @param victimUser victimUser's deck, and what can be stolen
	 * @return
	 */
	public Boolean canRobPlayer(TurnManager turnManager, User currUser, User victimUser) {
		return null;
	}
	
	/**
	 * If user can offer trade
	 * @param turnManager can only trade on user's turn 
	 * @param user if user has the card(s) offered
	 * @return
	 */
	public Boolean canOfferTrade(TurnManager turnManager, User user) {
		return null;
	}
	
	/**
	 * If user can accept a trade
	 * @param user if user has the card(s) required to accept trade
	 * @return
	 */
	public Boolean canAcceptTrade(User user) {
		return null;	
	}
	
	/**
	 * If user can end the turn
	 * @param turnManager check phase -- if all done, can end
	 * @return
	 */
	public Boolean canFinishTurn(TurnManager turnManager) {
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
	 * Get the {@link TradeManager} from the Game singleton
	 * @return The Game's TradeManager
	 */
	public TradeManager tradeManager() {
		return tradeManager;
	}
}
