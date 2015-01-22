package shared.proxy.games;

/**
 * Representation of a player used for listing available games.
 * @author Kent
 *
 */
public class Player {

	/**
	 * User's name
	 */
	private String name;
	/**
	 * Color of User's pieces
	 */
	private String color;
	/**
	 * User's ID
	 */
	private int id;
	/**
	 * Constructer to instantiate Player using given values
	 * @param name
	 * @param color
	 * @param id
	 */
	public Player(String name, String color, int id) {
		super();
		this.name = name;
		this.color = color;
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	
}
