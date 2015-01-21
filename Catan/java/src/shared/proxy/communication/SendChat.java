package shared.proxy.communication;

/**
 * Wrapper for determining what message is to be sent and which
 * User sent it
 * @author Kent
 *
 */
public class SendChat {

	/**
	 * Index of which player sent the chat
	 */
	private int PlayerIndex;
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
		PlayerIndex = playerIndex;
		this.content = content;
	}
	public int getPlayerIndex() {
		return PlayerIndex;
	}
	public void setPlayerIndex(int playerIndex) {
		PlayerIndex = playerIndex;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
