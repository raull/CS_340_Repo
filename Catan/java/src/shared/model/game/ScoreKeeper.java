package shared.model.game;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class keeps track of the score of every user. A user will win once it's score is 10.
 * @author Raul Lopez Villalpando
 *
 */
public class ScoreKeeper {

	private Map<Integer, Integer> score;
	private int winner;
	
	//Constructor
	
	/**
	 * Creates a new Score Keeper with the provided users's turn index. They will all start with 0 points
	 * @param users The users to start keeping the score
	 */
	public ScoreKeeper(ArrayList<User> users) {
		this.score = new TreeMap<Integer, Integer>();
		//Set it to 0 for all users
		for (User user : users) {
			this.score.put(new Integer(user.getTurnIndex()), new Integer(0));
		}
	}
	
	/**
	 * Creates a new Score Keeper. They will all start with 0 points
	 * @param users The number of users on the game
	 */
	public ScoreKeeper(int users) {
		this.score = new TreeMap<Integer, Integer>();
		for (int i = 0; i < users; i++) {
			this.score.put(new Integer(i), new Integer(0));	
		}
	}
	
	/**
	 * Get the score for a User
	 * @param user The user for the requested score
	 * @return The score of the user
	 */
	public Integer getScore(int turnIndex) {
		return this.score.get(new Integer(turnIndex));
	}
	
	/**
	 * Get the score for a User
	 * @param user The user for the requested score
	 * @return The score of the user
	 */
	public int getScoreValue(int turnIndex) {
		return this.score.get(new Integer(turnIndex)).intValue();
	}
	
	/**
	 * Set the score of the user at the turn index
	 * @param turnIndex The user's turn index to set the score
	 * @param score the score number
	 */
	public void setScore(int turnIndex, int score) {
		this.score.put(new Integer(turnIndex), new Integer(score));
	}

	/**
	 * The winner of the game
	 * @return -1 if there is no winner, 0-3 the turn index of the winner.
	 */
	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
	}

	
}
