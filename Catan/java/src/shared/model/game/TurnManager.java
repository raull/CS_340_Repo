package shared.model.game;

import java.util.ArrayList;

/**
 * The manager that keeps track of the user's turn
 * @author Raul Lopez
 *
 */
public class TurnManager {
	
	private ArrayList<User> users;
	/**
	 * The turn's current user
	 */
	private User currentUser;
	
	/**
	 * The index of the user whose current turn is
	 */
	private int currentTurn;
	
	/**
	 * The phase of the current user's turn
	 */
	private TurnPhase currentPhase;
	
	
	//Constructor
	
	/**
	 * Constructor that takes an arrayList of users to manage whose turn is it
	 * @param users The list of users to manage
	 */
	public TurnManager(ArrayList<User> users) {
		this.users = users;
	}
	
	//Helper Methods
	
	/**
	 * Returns the user for the provided turn index
	 * @param turnIndex The number of the turn
	 * @return Returns the user corresponding the turn index. If there was no user with that index, then it returns null.
	 */
	public User getUser(int turnIndex) {
		for (User user : users) {
			if (user.getTurnIndex() == turnIndex) {
				return user;
			}
		}
		
		return null;
	}
	
	//Getters and Setters
	/**
	 * Get the user with the current turn
	 * @return A {@link User} object that currently hold the turn of the game
	 */
	public User currentUser() {
		return currentUser;
	}
	
	/**
	 * Get the user with the current turn
	 * @return An int representing the current turn index
	 */
	public int currentTurn() {
		return currentTurn;
	}
	
	
	/**
	 * Get the current phase of the current turn
	 * @return A {@link TurnPhase} object representing the current phase of the turn
	 */
	public TurnPhase currentTurnPhase() {
		return currentPhase;
	}
	
}
