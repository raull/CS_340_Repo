package shared.proxy.communication;

/**
 * Returned after creating a game.
 * @author Kent
 *
 */
public class NewGame {

	/**
	 * Name of the game
	 */
	private String title;
	/**
	 * Game's Unique ID
	 */
	private int id;
	/**
	 * Players currently in game
	 */
	private EmptyPlayer[] players;
	
	/**
	 * Constructor to instantiate NewGame
	 * @param title
	 * @param id
	 * @param players
	 */
	public NewGame(String title, int id, EmptyPlayer[] players) {
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

	public EmptyPlayer[] getPlayers() {
		return players;
	}

	public void setPlayers(EmptyPlayer[] players) {
		this.players = players;
	}
	
	
}
