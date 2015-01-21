package shared.proxy.communication;

/**
 * A request sent to the server to join a specific game
 * @author Kent
 *
 */
public class JoinGameRequest {

	/**
	 * The ID of the game requesting to join
	 */
	private int id;
	/**
	 * The requested color for this player for that game
	 */
	private String color;
	
	/**
	 * Constructor that instantiates the JoinGameRequest
	 * @param id
	 * @param color
	 */
	public JoinGameRequest(int id, String color) {
		super();
		this.id = id;
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
