package shared.proxy.games;

/**
 * Used primarily for debugging purposes, saves the state of the game 
 * to a file
 * @author Kent
 *
 */
public class SaveGameRequest {

	/**
	 * The ID of the game to be saved
	 */
	private int id;
	/**
	 * The file name to be saved under
	 */
	private String name;
	
	/**
	 * Constructor to instantiate the request
	 * @param id
	 * @param name
	 */
	public SaveGameRequest(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
