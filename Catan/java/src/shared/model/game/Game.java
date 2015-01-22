package shared.model.game;

import shared.model.board.Map;
import shared.model.cards.Bank;

/**
 * A class that represents the clients game. It is designed as a singleton pattern for the easiness of the client
 * @author Raul Lopez
 *
 */
public class Game {
	
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
	 * The games's {@link UserManager} class to keep track of all users
	 */
	private UserManager userManager;
	/**
	 * The games's {@link Map}
	 */
	private Map map;
	/**
	 * The games's {@link Bank} class to keep track of the game's decks
	 */
	private Bank bank;
	/**
	 * Singleton instance of the game
	 */
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
	public static Bank bank() {
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
