package shared.model.game;

import java.util.ArrayList;
import java.util.Collections;

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
	 * The number of the last rolled dice
	 */
	private int rolledNumber;
	
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
	public User getUserFromIndex(int turnIndex) {
		for (User user : users) {
			if (user.getTurnIndex() == turnIndex) {
				return user;
			}
		}
		
		return null;
	}
	
	/**
	 * Returns the user for the provided turn index
	 * @param turnIndex The number of the turn
	 * @return Returns the user corresponding the turn index. If there was no user with that index, then it returns null.
	 */
	public User getUser(int id) {
		for (User user : users) {
			if (user.getPlayerID() == id) {
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
	
	/**
	 * Get a read-only list of users
	 * @return The list of users
	 */
	public ArrayList<User> getUsers() {
		return (ArrayList<User>) Collections.unmodifiableCollection(this.users);
	}

	public int getRolledNumber() {
		return rolledNumber;
	}

	public void setRolledNumber(int rolledNumber) {
		this.rolledNumber = rolledNumber;
	}
	
}
