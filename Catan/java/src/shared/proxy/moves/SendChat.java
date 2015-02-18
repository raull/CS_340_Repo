package shared.proxy.moves;

/**
 * Wrapper for determining what message is to be sent and which
 * User sent it
 * @author Kent
 *
 */
public class SendChat {

	private String type;
	/**
	 * Index of which player sent the chat
	 */
	private int playerIndex;
	/**
	 * The message being sent. 
	 */
	private String content;
	/**
	 * Constructor to instantiate the SendChat object.
	 * @param playerIndex
	 * @param content
	 */
	public SendChat(int playerIndex, String content) {
		super();
		playerIndex = playerIndex;
		this.content = content;
		type = "sendChat";
	}
	public int getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		playerIndex = playerIndex;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
