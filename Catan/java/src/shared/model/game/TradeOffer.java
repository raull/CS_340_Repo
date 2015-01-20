package shared.model.game;

import shared.definitions.ResourceType;

/**
 * This class represents a trade offer
 * @author Raul Lopez
 *
 */
public class TradeOffer {

	private int rate;
	private ResourceType buy;
	private ResourceType sell;
	
	/**
	 * Create a new TradeOfferClass
	 * @param offerRate The rate in which the offer will be performed
	 * @param sellResource The resource that will be given
	 * @param buyResource The resource that will be received
	 */
	public TradeOffer(int offerRate, ResourceType sellResource, ResourceType buyResource) {
		this.rate = offerRate;
		this.sell = sellResource;
		this.buy = buyResource;
	}

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	public ResourceType getBuy() {
		return buy;
	}

	public void setBuy(ResourceType buy) {
		this.buy = buy;
	}

	public ResourceType getSell() {
		return sell;
	}

	public void setSell(ResourceType sell) {
		this.sell = sell;
	}
	
}
