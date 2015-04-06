package server.persistence.dao;

import java.util.List;

import server.game.Game;

/**
 * Parent class to determine what a DAO for a Game should implement
 * @author raulvillalpando
 *
 */
public abstract class GameDAOInterface implements DAOInterface {
	
	/**
	 * Create a new Game
	 * @param name The title or name of the new game.
	 * @param randomTiles Boolean to indicate if the game will have random tile locations.
	 * @param randomPorts Boolean to indicate if the game will have random port locations.
	 * @param randomNumbers Boolean to indicate if the game will have random number locations.
	 * @return True if the game got added successfully, False otherwise.
	 */
	abstract public boolean createGame(String name, boolean randomTiles, boolean randomPorts, boolean randomNumbers);
	
	/**
	 * Get a game with the specified ID.
	 * @param gameId The ID of the game to fetch.
	 * @return The game with the ID provided.
	 */
	abstract public Game getGame(int gameId);
	
	/**
	 * List all the game created and currently playing
	 * @return A list of all the games created and currently playing.
	 */
	abstract public List<Game> listGames();
	
	/**
	 * Save or update the state of the existing game
	 * @param game The game that contains the ID and all the information to update
	 * @return True if the game got updated or saved successfully, False otherwise.
	 */
	abstract public boolean saveGame(Game game);

}
