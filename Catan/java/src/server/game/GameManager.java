package server.game;

import java.util.ArrayList;
import java.util.List;

/**
 * This manager stores and retrieves all Games on the current server. This class is meant to be a singleton so the server can access it from anywhere.
 * @author raulvillalpando
 *
 */
public class GameManager {
	
	/**
	 * A list of games stored on the server
	 */
	private List<Game> games;
	
	public static GameManager instance;
	
	private GameManager() {
		games = new ArrayList<Game>();
	}
	
	/**
	 * The singleton instance of GameManager
	 * @return
	 */
	public GameManager instance() {
		if (GameManager.instance == null) {
			instance = new GameManager();
		}
		
		return instance;
	}
	
	/**
	 * Adds a new game to the existing list
	 * @param newGame
	 */
	public void addGame(Game newGame) {
		games.add(newGame);
	}
	
	/**
	 * Get a game by its id
	 * @param id The ID of the game to retrieve
	 * @return
	 */
	public Game getGameById(int id) {
		return null;
	}
	
	/**
	 * Get the list of existing games
	 * @return
	 */
	public List<Game> getGames() {
		return games;
	}
}
