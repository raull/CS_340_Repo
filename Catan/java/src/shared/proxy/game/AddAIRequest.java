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
	private String AItype;

	/**
	 * Constructor to instantiate AddAIRequest object
	 * @return
	 */
	public String getAItype() {
		return AItype;
	}

	public void setAItype(String aItype) {
		AItype = aItype;
	}

	public AddAIRequest(String aItype) {
		super();
		AItype = aItype;
	}
}
