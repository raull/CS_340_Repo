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
	private Integer playerIndex;
	/**
	 * The message being sent. 
	 */
	private String content;
	/**
	 * Constructor to instantiate the SendChat object.
	 * @param playerIndex
	 * @param content
	 */
	public SendChat(Integer playerIndex, String content) {
		super();
		this.playerIndex = playerIndex;
		this.content = content;
		type = "sendChat";
	}
	public Integer getPlayerIndex() {
		return playerIndex;
	}
	public void setPlayerIndex(Integer playerIndex) {
		this.playerIndex = playerIndex;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
