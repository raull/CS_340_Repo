package shared.proxy.communication;
/**
 * Used to accept or decline a domestic trade
 * @author Kent
 *
 */
public class AcceptTrade {

	/**
	 * Who is accepting or declining the trade
	 */
	private int playerIndex;
	/**
	 * Whether the trade is accepted or not
	 */
	private boolean willAccept;
	
	/**
	 * Constructor to instantiate the AcceptTrade object
	 * @param playerIndex
	 * @param willAccept
	 */
	public AcceptTrade(int playerIndex, boolean willAccept) {
		super();
		this.playerIndex = playerIndex;
		this.willAccept = willAccept;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public boolean isWillAccept() {
		return willAccept;
	}

	public void setWillAccept(boolean willAccept) {
		this.willAccept = willAccept;
	}
}
