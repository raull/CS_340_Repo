package shared.proxy.games;

/**
 * Information sent to the server to request that a new game be created
 * @author Kent
 *
 */
public class CreateGameRequest {

	/**
	 * A boolean used to decide if Tiles are randomly placed
	 */
	private boolean randomTiles;
	/**
	 * Whether the numbers should be randomly placed
	 */
	private boolean randomNumbers;
	/**
	 * Whether the ports should be randomly placed
	 */
	private boolean randomPorts;
	/**
	 * The name of the game.
	 */
	private String name;
	/**
	 * Constructor used to instantiate all fields
	 * @param randomTiles
	 * @param randomNumbers
	 * @param randomPorts
	 * @param name
	 */
	public CreateGameRequest(boolean randomTiles, boolean randomNumbers,
			boolean randomPorts, String name) {
		super();
		this.randomTiles = randomTiles;
		this.randomNumbers = randomNumbers;
		this.randomPorts = randomPorts;
		this.name = name;
	}
	public boolean isRandomTiles() {
		return randomTiles;
	}
	public void setRandomTiles(boolean randomTiles) {
		this.randomTiles = randomTiles;
	}
	public boolean isRandomNumbers() {
		return randomNumbers;
	}
	public void setRandomNumbers(boolean randomNumbers) {
		this.randomNumbers = randomNumbers;
	}
	public boolean isRandomPorts() {
		return randomPorts;
	}
	public void setRandomPorts(boolean randomPorts) {
		this.randomPorts = randomPorts;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
