package shared.proxy.communication;
/**
 * Communication object (wrapper) used to execute a Maritime trade
 * @author Kent
 *
 */
public class MaritimeTrade {

	/**
	 * Who is doing the Maritime trade
	 */
	private int playerIndex;
	/**
	 * 	The ratio of the trade (e.g. 3 for a 3:1 trade)
	 */
	private int ratio;
	/**
	 * The resource you're getting
	 */
	private int outputResource;
	/**
	 * The resource you're giving
	 */
	private int inputResource;
	
	/**
	 * Constructor for the MaritimeTrade object
	 * @param playerIndex
	 * @param ratio
	 * @param outputResource
	 * @param inputResource
	 */
	public MaritimeTrade(int playerIndex, int ratio, int outputResource,
			int inputResource) {
		super();
		this.playerIndex = playerIndex;
		this.ratio = ratio;
		this.outputResource = outputResource;
		this.inputResource = inputResource;
	}

	public int getPlayerIndex() {
		return playerIndex;
	}

	public void setPlayerIndex(int playerIndex) {
		this.playerIndex = playerIndex;
	}

	public int getRatio() {
		return ratio;
	}

	public void setRatio(int ratio) {
		this.ratio = ratio;
	}

	public int getOutputResource() {
		return outputResource;
	}

	public void setOutputResource(int outputResource) {
		this.outputResource = outputResource;
	}

	public int getInputResource() {
		return inputResource;
	}

	public void setInputResource(int inputResource) {
		this.inputResource = inputResource;
	}
}
