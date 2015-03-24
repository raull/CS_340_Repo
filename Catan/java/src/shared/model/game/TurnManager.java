package shared.model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import shared.model.exception.ModelException;

/**
 * The manager that keeps track of the user's turn
 * @author Raul Lopez
 *
 */
public class TurnManager {
	
	private ArrayList<User> users;
	
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
	
	/**
	 * user index with the longest road
	 */
	private int longestRoadIndex;
	
	/**
	 * user index with largest army
	 */
	private int largestArmyIndex;
	
	//Constructor
	
	/**
	 * Constructor that takes an arrayList of users to manage whose turn is it
	 * @param users The list of users to manage
	 */
	public TurnManager(ArrayList<User> users) {
		this.users = users;
		currentPhase = TurnPhase.FIRSTROUND;
	}
	
	//Helper Methods
	
	/**
	 * Returns the user for the provided turn index
	 * @param turnIndex The user's turn number
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
	
	public User getUserFromID(int id) {
		for (User user : users) {
			if (user.getPlayerID() == id) {
				return user;
			}
		}
		
		return null;
	}
	
	public User getUserFromName(String name)
	{
		for (User user : users)
		{
			if (user.getName().equals(name))
			{
				return user;
			}
		}
		System.out.println("User.getUserFromName returning null!!");
		return null;
	}
	
	/**
	 * Adds a user to the user array
	 * @param newUser the user to be added
	 * @throws ModelException if too many players are added
	 */
	public void addUser(User newUser) throws ModelException {
		
		int index = users.size();
		
		if(users.size() < 4) {
			User userToAdd = newUser.clone();
			newUser.setPlayerTurnIndex(index);
			users.add(userToAdd);
		}
		else{
			throw new ModelException();
		}
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
		return this.users.get(this.currentTurn);
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
	public List<User> getUsers() {
		return Collections.unmodifiableList(this.users);
	}
	

	public int getRolledNumber() {
		return rolledNumber;
	}

	public void setRolledNumber(int rolledNumber) {
		this.rolledNumber = rolledNumber;
	}
	
	/**
	 * Get the user with the current turn
	 * @return An int representing the current turn index
	 */
	public int getCurrentTurn() {
		return currentTurn;
	}

	public void setCurrentTurn(int currentTurn) {
		this.currentTurn = currentTurn;
	}

	public void setCurrentPhase(TurnPhase currentPhase) {
		this.currentPhase = currentPhase;
	}

	public int getLogestRoadIndex() {
		return longestRoadIndex;
	}

	public void setLongestRoadIndex(int longestRoadIndex) {
		this.longestRoadIndex = longestRoadIndex;
	}

	public int getLargestArmyIndex() {
		return largestArmyIndex;
	}

	public void setLargestArmyIndex(int largestArmyIndex) {
		this.largestArmyIndex = largestArmyIndex;
	}

}
