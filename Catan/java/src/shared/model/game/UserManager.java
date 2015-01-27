package shared.model.game;

import java.util.ArrayList;


/**
 * UserManger class manages the users and can change the player list, tell whose turn it is, and cycle through turns
 * 
 * @author thyer
 *
 */
public class UserManager {
	private ArrayList <User> players;
	
	public UserManager(){
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
	

}
