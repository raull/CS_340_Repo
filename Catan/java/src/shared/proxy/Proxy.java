package shared.proxy;

import java.util.List;

import shared.proxy.communication.*;
/**
 * Used by the Client to connect to the Server and perform
 * necessary functions upon it.
 * @author Kent
 *
 */
public class Proxy {
	
	/**
	 * Takes a User's credentials to log them into the server
	 * @param cred
	 */
	public void login(Credentials cred){
		//CODE
	}
	/**
	 * Takes a User's credentials to register them on the server.
	 * @param cred
	 */
	public void register(Credentials cred){
		//CODE
	}
	/**
	 * Returns a list of all games in progress and their players.
	 * @return
	 */
	public List<Game> list(){
		return null;
		// CODE
	}
	/**
	 * Requests that a new game be created.
	 * @param CreateRequest
	 * @return
	 */
	public  NewGame create(CreateGameRequest CreateRequest){
		return null;
		//CODE
	}
	/**
	 * Requests to join a particular game, with a specific color for the player
	 * @param JoinRequest
	 */
	public void join (JoinGameRequest JoinRequest){
		//CODE
	}
	/**
	 * Requests that the current game state be saved to a file
	 * @param SaveRequest
	 */
	public void save(SaveGameRequest SaveRequest){
		//CODE
	}
	/**
	 * Requests that a game be loaded from a saved file
	 * @param LoadRequest
	 */
	public void load(LoadGameRequest LoadRequest){
		//CODE
	}
	
	public 
}
