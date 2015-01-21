package shared.proxy.communication;

/**
 * Requests that a saved game be loaded from a file
 * @author Kent
 *
 */
public class LoadGameRequest {

	/**
	 * The name of the file to be loaded
	 */
	private String name;

	/**
	 * Constructor to instantiate the request
	 * @param name
	 */
	public LoadGameRequest(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
