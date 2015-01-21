package shared.model.model_facade;

import shared.model.board.Map;
import shared.model.cards.DevCardDeck;
import shared.model.cards.Hand;
import shared.model.game.ScoreKeeper;
import shared.model.game.TurnManager;
import shared.proxy.Proxy;

public class ModelFacade {
	//canDo functions
	//get pieces from models
	//will eventually have to talk to controllers
	Proxy serverProxy; //has a pointer to the server proxy to see if methods that user has called is valid
	
	/**
	 * Determines if user can buy a road
	 * @param turnManager if it is user's turn
	 * @param userHand if user has the proper resources
	 * @return boolean, whether user can buy road
	 */
	public Boolean canBuyRoad(TurnManager turnManager, Hand userHand) {
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
	 * @param userHand if user has the proper resources
	 * @return
	 */
	public Boolean canBuyBuilding(TurnManager turnManager, Hand userHand) {
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
	 * @param userHand if user has proper resources
	 * @param devCardDeck if there are dev cards to buy
	 * @return
	 */
	public Boolean canBuyDevCard(TurnManager turnManager, Hand userHand, DevCardDeck devCardDeck) {
		return null;
	}
	
	/**
	 * If user can play the dev card
	 * @param turnManager if it is user's turn
	 * @param userHand if user has the card
	 * @return
	 */
	public Boolean canPlayDevCard(TurnManager turnManager, Hand userHand) {
		return null;
	}
	
	/**
	 * If user can offer trade
	 * @param turnManager can only trade on user's turn 
	 * @param userHand if user has the card(s) offered
	 * @return
	 */
	public Boolean canOfferTrade(TurnManager turnManager, Hand userHand) {
		return null;
	}
	
	/**
	 * If user can accept a trade
	 * @param userHand if user has the card(s) required to accept trade
	 * @return
	 */
	public Boolean canAcceptTrade(Hand userHand) {
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
}
