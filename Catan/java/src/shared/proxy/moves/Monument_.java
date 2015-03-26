package shared.proxy.moves;

/**
 * Used to play a "Monument" card from your hand to get a victory point.
 * @author Kent
 *
 */
public class Monument_ {

	private String type;
	/**
	 * Who's playing this dev. card.
	 */
	private Integer playerIndex;

	/**
	 * Constructor to instantiate the Monument_ object
	 * @param playerIndex
	 */
	public Monument_(Integer playerIndex) {
		super();
		this.playerIndex = playerIndex;
		type = "Monument";
	}

	public Integer getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(Integer playerIndex) {
		this.playerIndex = playerIndex;
	}
	
}
