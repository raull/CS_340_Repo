package shared.model;

import shared.model.board.Port;
import shared.model.exception.InvalidMoveException;

/**
 * This class manages the trades between user-user and user-port
 * @author Raul Lopez Villalpando
 *
 */
public class TradeManager {
	
	/**
	 * Make an offer between users
	 * @param user1 The user making the offer
	 * @param user2 The user receiving the offer
	 * @param offer The offer that the first user is making
	 * @throws InvalidMoveException
	 */
	public void makeOffer(User user1, User user2, TradeOffer offer) throws InvalidMoveException {
		
	}
	
	/**
	 * Returns either if the offer can be made (if it's valid)
	 * @param user1 The user making the offer
	 * @param user2 The user receiving the offer
	 * @param offer The offer that the first user is making
	 * @return Whether or not the offer is valid
	 */
	public boolean canMakeOffer(User user1, User user2, TradeOffer offer) {
		return false;
	}
	
	/**
	 * Make an offer between port and user
	 * @param user The user making the offer
	 * @param port The port receiving the offer
	 * @param offer The offer that the first user is making
	 * @throws InvalidMoveException
	 */
	public void makeOffer(User user, Port port, TradeOffer offer) throws InvalidMoveException {
		
	}
	
	/**
	 * Returns either if the offer can be made (if it's valid)
	 * @param user The user making the offer
	 * @param port The port receiving the offer
	 * @param offer The offer that the first user is making
	 * @return Whether or not the offer is valid
	 */
	public boolean canMakeOffer(User user, Port port, TradeOffer offer) {
		return false;
	}
	
}
