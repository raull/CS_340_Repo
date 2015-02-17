package client.manager;

import java.util.ArrayList;

import shared.model.facade.ModelFacade;
import shared.model.game.User;
import shared.proxy.ServerProxy;


/**
 * Client Manager is a singleton class that handles the {@link ModelFacade} and {@link ServerProxy}
 * for easier access. This class alse serves to store values for easy access as well, for example the user ID that the client is currently logged in as
 * 
 * @author thyer
 *
 */
public class ClientManager {
	
	private static ClientManager instance;
	private static ArrayList <User> players;
	private static ServerProxy serverProxy = new ServerProxy();
	private static ModelFacade modelFacade = new ModelFacade();
	private static User currentUser;
	
	private ClientManager() {
	}
	
	public static ClientManager instance(){
		if (instance != null) {
			return instance;
		} else {
			return new ClientManager();
		}
	}
	
	/**
	 * Calls canAddUser, if that returns true, adds the new user to the player list
	 * @param newPlayer the player to be added
	 */
	public void addUserToGame(User newPlayer){
		players.add(newPlayer);
		//TODO: throw exception if player can't be added
	}
	
	/**
	 * Checks to see if the Player is logged in and if the list is sufficiently small.
	 * @param newPlayer
	 * @return true if the player can be added, false otherwise
	 */
	public boolean canUserJoinGame(User newPlayer){
		return true;
	}
	
	/**
	 * Removes a given player from the list (not sure if we'd need this)
	 * @param toBeRemovedPlayer
	 */
	public void removePlayer(User toBeRemovedPlayer){
		
	}
	
	/**
	 * Get the client Model Facade
	 * @return The singleton copy of the Model Facade
	 */
	public ModelFacade getModelFacade() {
		return modelFacade;
	}
	
	/**
	 * Get the client Server Proxy
	 * @return The singleton copy of the Server Proxy
	 */
	public ServerProxy getServerProxy() {
		return serverProxy;
	}
	
	/**
	 * Get the current logged in User
	 * @return the current logged in user
	 */
	public User getCurrentUser() {
		return currentUser;
	}

}
