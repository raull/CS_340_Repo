package shared.proxy.moves;
/**
 * Communication object (wrapper) used to execute a Maritime trade
 * @author Kent
 *
 */
public class MaritimeTrade {

	private String type;
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
	private String outputResource;
	/**
	 * The resource you're giving
	 */
	private String inputResource;
	
	/**
	 * Constructor for the MaritimeTrade object
	 * @param playerIndex
	 * @param ratio
	 * @param outputResource
	 * @param inputResource
	 */
	public MaritimeTrade(int playerIndex, int ratio, String outputResource,
			String inputResource) {
		super();
		this.playerIndex = playerIndex;
		this.ratio = ratio;
		this.outputResource = outputResource;
		this.inputResource = inputResource;
		type = "maritimeTrade";
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

	public String getOutputResource() {
		return outputResource;
	}

	public void setOutputResource(String outputResource) {
		this.outputResource = outputResource;
	}

	public String getInputResource() {
		return inputResource;
	}

	public void setInputResource(String inputResource) {
		this.inputResource = inputResource;
	}
}
