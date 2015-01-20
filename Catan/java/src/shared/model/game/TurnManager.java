package shared.model.game;

import java.util.ArrayList;

public class TurnManager {
	
	private ArrayList<User> users;
	private User currentUser;
	private TurnPhase currentPhase;
	
	
	//Contructor
	
	/**
	 * Constructor that takes an arrayList of users to manage whose turn is it
	 * @param users The list of users to manage
	 */
	public TurnManager(ArrayList<User> users) {
		
	}
	
	
	/**
	 * Get the user with the current turn
	 * @return A {@link User} object that currently hold the turn of the game
	 */
	public User currentUser() {
		return currentUser;
	}
	
	/**
	 * Get the current phase of the current turn
	 * @return A {@link TurnPhase} object representing the current phase of the turn
	 */
	public TurnPhase currentTurnPhase() {
		return currentPhase;
	}
	
	/**
	 * End the turn of the current user and start the next user's turn
	 */
	public void endTurn() {
		
	}
	
}
