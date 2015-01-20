package shared.model.game;

import java.util.ArrayList;
import java.util.Map;

/**
 * This class keeps track of the score of every user. A user will win once it's score is 10.
 * @author Raul Lopez Villalpando
 *
 */
public class ScoreKeeper {

	private Map<User, Integer> score;
	
	//Constructor
	
	/**
	 * Creates a new Score Keeper with the provided users. They will all start with 0
	 * @param users The users to start keeping the score
	 */
	public ScoreKeeper(ArrayList<User> users) {
		
	}
	
	
	/**
	 * Get the score for a User
	 * @param user The user for the requested score
	 * @return The score of the user
	 */
	public int getScore(User user) {
		return 0;
	}
	
	/**
	 * Increment the score of the user by 1 point
	 * @param user The user to increment the score
	 */
	public void incrementScore(User user) {
		
	}
	
	/**
	 * Return the winner of the game
	 * @return The winner of the game. If there is none, return null.
	 */
	public User winner() {
		return null;
	}
	
}
