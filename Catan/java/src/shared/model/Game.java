package shared.model;

import shared.model.board.Map;
import shared.model.cards.Bank;

/**
 * A class that represents the clients game. It is designed as a singleton pattern for the easiness of the client
 * @author Raul Lopez
 *
 */
public class Game {
	
	private ScoreKeeper score;
	private TradeManager tradeManager;
	private TurnManager turnManager;
	private UserManager userManager;
	private Map map;
	private Bank bank;
	
	private static Game instance = null;
	
	/**
	 * Get the singleton instance of the <code>Game</code> class
	 * @return The Games singleton instance
	 */
	public static Game singleton() {
		if (instance == null) {
			instance = new Game();
			return instance;
		}
		return instance;
	}
	
	
	//Getters and Setters
	/**
	 * Get the {@link Bank} from the Game singleton
	 * @return The Game's Bank
	 */
	public static Bank banck() {
		return Game.singleton().bank;
	}
	
	/**
	 * Get the {@link ScoreKeeper} from the Game singleton
	 * @return The Game's ScoreKeeper
	 */
	public static ScoreKeeper score() {
		return Game.singleton().score;
	}
	
	/**
	 * Get the {@link TurnManager} from the Game singleton
	 * @return The Game's TurnManager
	 */
	public static TurnManager turnManager() {
		return Game.singleton().turnManager;
	}
	
	/**
	 * Get the {@link Map} from the Game singleton
	 * @return The Game's Map
	 */
	public static Map map() {
		return Game.singleton().map;
	}
	
	/**
	 * Get the {@link TradeManager} from the Game singleton
	 * @return The Game's TradeManager
	 */
	public static TradeManager tradeManager() {
		return Game.singleton().tradeManager;
	}
	
	/**
	 * Get the {@link UserManager} from the Game singleton
	 * @return The Game's UserManager
	 */
	public static UserManager userManager() {
		return Game.singleton().userManager;
	}
	
}
