package server.facade;

import java.util.List;

import server.exception.ServerInvalidRequestException;
import server.game.Game;
import shared.model.Model;

public class ServerFacade {

	/**
	 * Validates the player's credentials, and logs them in to the server.
	 * @param username The user's username (case-sensitive)
	 * @param password The user's password (case-sensitive)
	 * @throws ServerInvalidRequestException 
	 */
	public void login(String username, String password) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * Creates a new player account, and logs them in to the server
	 * @param username The user's username (case-sensitive)
	 * @param password The user's password (case-sensitive)
	 * @throws ServerInvalidRequestException
	 */
	public void register(String username, String password) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * Get a list of all games in progress.
	 * @return The list of games currently in progress
	 * @throws ServerInvalidRequestException
	 */
	public List<Game> gameList() throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Creates and returns a new game.
	 * @param name The name of the new game.
	 * @param randomTiles Boolean describing if the new game should have random tile locations.
	 * @param randomNumbers Boolean describing if the new game should have the numbers randomly allocated on the map.
	 * @param randomPorts Boolean describing if the location of the ports should be random on the new game.
	 * @return A newly created game.
	 * @throws ServerInvalidRequestException
	 */
	public Game createNewGame(String name, boolean randomTiles, boolean randomNumbers, boolean randomPorts) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Adds (or re-adds) the player to the specified game
	 * @param gameId The ID of the game to join
	 * @param color The color of the player for the game to join. Should not be taken by another player already.
	 * @throws ServerInvalidRequestException
	 */
	public void joinGame(int gameId, String color) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * This method is for testing and debugging purposes. 
	 * When a bug is found, you can use the /games/save method to save 
	 * the state of the game to a file, and attach the file to a bug report. 
	 * A developer can later restore the state of the game when the bug 
	 * occurred by loading the previously saved file using the /games/load method. 
	 * Game files are saved to and loaded from the server's saves/ directory.
	 * @param gameId The ID of the game to save.
	 * @param fileName The file name you want to save it under
	 * @throws ServerInvalidRequestException
	 */
	public void gameSave(int gameId, String fileName) throws ServerInvalidRequestException {
		
	}
	
	/**
	 * This method is for testing and debugging purposes. When a bug is found, 
	 * you can use the /games/save method to save the state of the game to a file, 
	 * and attach the file to a bug report. A developer can later restore the state 
	 * of the game when the bug occurred by loading the previously saved file using 
	 * the /games/load method. Game files are saved to and loaded from the server's 
	 * saves/ directory.
	 * @param fileName The name of the saved game file that you want to load. (The game's ID is restored as well.)
	 * @return The game that was previously saved.
	 * @throws ServerInvalidRequestException
	 */
	public Game gameLoad(String fileName) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * Return the model
	 * @param version The version number of the model that the caller already has. 
	 * It goes up by one for each command that is applied. If you send this parameter, 
	 * you will get a model back only if the current model is newer than the specified 
	 * version number. Otherwise, it returns the string "true" to notify the caller that 
	 * it already has the current model state.
	 * @param game The game from which the model comes from.
	 * @return
	 * @throws ServerInvalidRequestException
	 */
	public Model getModel(int version, Game game) throws ServerInvalidRequestException {
		return null;
	}
	
	/**
	 * For the default games created by the server, this method reverts the game to the 
	 * state immediately after the initial placement round. For user-created games, this 
	 * method reverts the game to the very beginning (i.e., before the initial placement 
	 * round). This method returns the client model JSON for the game after it has been 
	 * reset.
	 * @param game The game to reset.
	 * @throws ServerInvalidRequestException
	 */
	public Model resetGame(Game game) throws ServerInvalidRequestException {
		return null;
	}

}
