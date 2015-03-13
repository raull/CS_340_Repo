package server.game;

import java.util.ArrayList;
import java.util.List;

public class GameManager {
	private List<Game> games;
	
	public GameManager() {
		games = new ArrayList<Game>();
	}
	
	/**
	 * adds a new game to the existing list
	 * @param newGame
	 */
	public void addGame(Game newGame) {
		games.add(newGame);
	}
	
	/**
	 * get a game by its id
	 * @param id
	 * @return
	 */
	public Game getGameById(int id) {
		return null;
	}
	
	/**
	 * get the list of existing games
	 * @return
	 */
	public List<Game> getGames() {
		return games;
	}
}
