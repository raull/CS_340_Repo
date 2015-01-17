package shared.model;

import java.util.ArrayList;


/**
 * UserManger class manages the users and can change the player list, tell whose turn it is, and cycle through turns
 * 
 * @author thyer
 *
 */
public class UserManager {
	private ArrayList <Player> players;
	private int currentPlayerIndex;
	private Player currentPlayer;
	
	public UserManager(){
	}
	
	/**
	 * Calls canAddUser, if that returns true, adds the new user to the player list
	 * @param newPlayer the player to be added
	 */
	public void addUser(Player newPlayer){
		players.add(newPlayer);
		//TODO: throw exception if player can't be added
	}
	
	/**
	 * Checks to see if the Player looks okay and if the list is sufficiently small. 
	 * @param newPlayer
	 * @return true if the player can be added, false otherwise
	 */
	public boolean canAddUser(Player newPlayer){
		return true;
	}
	
	/**
	 * Changes the current player to the next one in the list (or the first player if the end is reached).
	 * Updates the currentPlayer and currentPlayerIndex
	 */
	public void cycleTurn(){
		
	}
	
	public Player getCurrentPlayer(){
		return new Player(null, null);
	}
	
	/**
	 * Removes a given player from the list (not sure if we'd need this)
	 * @param toBeRemovedPlayer
	 */
	public void removePlayer(Player toBeRemovedPlayer){
		
	}

}
