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
	private Boolean randomTiles;
	/**
	 * Whether the numbers should be randomly placed
	 */
	private Boolean randomNumbers;
	/**
	 * Whether the ports should be randomly placed
	 */
	private Boolean randomPorts;
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
	public CreateGameRequest(Boolean randomTiles, Boolean randomNumbers,
			Boolean randomPorts, String name) {
		super();
		this.randomTiles = randomTiles;
		this.randomNumbers = randomNumbers;
		this.randomPorts = randomPorts;
		this.name = name;
	}
	public Boolean isRandomTiles() {
		return randomTiles;
	}
	public void setRandomTiles(Boolean randomTiles) {
		this.randomTiles = randomTiles;
	}
	public Boolean isRandomNumbers() {
		return randomNumbers;
	}
	public void setRandomNumbers(Boolean randomNumbers) {
		this.randomNumbers = randomNumbers;
	}
	public Boolean isRandomPorts() {
		return randomPorts;
	}
	public void setRandomPorts(Boolean randomPorts) {
		this.randomPorts = randomPorts;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
