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
	private int currentPlayerIndex;
	private User currentPlayer;
	
	public UserManager(){
	}
	
	/**
	 * Calls canAddUser, if that returns true, adds the new user to the player list
	 * @param newPlayer the player to be added
	 */
	public void addUser(User newPlayer){
		players.add(newPlayer);
		//TODO: throw exception if player can't be added
	}
	
	/**
	 * Checks to see if the Player looks okay and if the list is sufficiently small. 
	 * @param newPlayer
	 * @return true if the player can be added, false otherwise
	 */
	public boolean canAddUser(User newPlayer){
		return true;
	}
	
	/**
	 * Changes the current player to the next one in the list (or the first player if the end is reached).
	 * Updates the currentPlayer and currentPlayerIndex
	 */
	public void cycleTurn(){
		
	}
	
	public User getCurrentPlayer(){
		return new User(null, null, null);
	}
	
	/**
	 * Removes a given player from the list (not sure if we'd need this)
	 * @param toBeRemovedPlayer
	 */
	public void removePlayer(User toBeRemovedPlayer){
		
	}

}
