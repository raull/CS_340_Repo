package shared.model.board;

import shared.definitions.PortType;


/**
 * This class represent a Port on a {@link Map}
 * @author Raul Lopez
 *
 */
public class Port {

	/**
	 * {@link PortType} representing the resource that the <code>Port</code> may provide
	 */
	private PortType type;
	/**
	 * An integer representing the offer rate for the resource that it provides, must be either 2 or 3
	 */
	private int offerRate;
	
	//Constructors
	/**
	 * Port Constructor for type and offerRate.
	 * @param type {@link PortType} representing the resource that the <code>Port</code>
	 * @param offerRate An integer representing the offer rate for the resource that it provides, must be either 2 or 3
	 */
	public Port(PortType type, int offerRate) {
		this.type = type;
		this.offerRate = offerRate;
	}
	
	//Getters
	public PortType getType() {
		return type;
	}
	public int getOfferRate() {
		return offerRate;
	}
}