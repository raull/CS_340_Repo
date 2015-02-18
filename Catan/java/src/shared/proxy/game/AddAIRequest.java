package shared.proxy.game;
/**
 * Used to add an AI player to the game
 * @author Kent
 *
 */
public class AddAIRequest {

	/**
	 * The type of AI to be added
	 */
	private String AIType;

	/**
	 * Constructor to instantiate AddAIRequest object
	 * @return
	 */
	public String getAIType() {
		return AIType;
	}

	public void setAItype(String aItype) {
		AIType = aItype;
	}

	public AddAIRequest(String aItype) {
		super();
		AIType = aItype;
	}
}
