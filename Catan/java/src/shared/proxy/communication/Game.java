package shared.proxy.communication;

/**
 * Simple representation of a game used in displaying available games
 * @author Kent
 *
 */
public class Game {

	/**
	 * Title of this game
	 */
	private String title;
	/**
	 * Game's unique ID
	 */
	private int id;
	/**
	 * The players currently in this game
	 */
	private Player[] players;
	
	/**
	 * Constructor to instantiate the Game.
	 * @param title
	 * @param id
	 * @param players
	 */
	public Game(String title, int id, Player[] players) {
		super();
		this.title = title;
		this.id = id;
		this.players = players;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Player[] getPlayers() {
		return players;
	}
	public void setPlayers(Player[] players) {
		this.players = players;
	}
	
	
}
