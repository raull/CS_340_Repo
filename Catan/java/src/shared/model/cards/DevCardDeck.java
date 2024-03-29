package shared.model.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import shared.definitions.DevCardType;
import shared.model.exception.InvalidMoveException;

/**
 * A collection of Development cards
 * @author Raul Lopez
 *
 */
public class DevCardDeck {
	private ArrayList<DevCard> cards = new ArrayList<DevCard>();
	
	/**
	 * Constructor for the deck
	 */
	public DevCardDeck(ArrayList<DevCard> initDevCards){
		cards = initDevCards;
	}
	
	public DevCardDeck() {
		
	}
	
	/**
	 * add a card to deck
	 * @param card
	 */
	public void addDevCard(DevCard card) {
		cards.add(card);
	}
	
	/**
	 * remove a card from deck
	 * @param card
	 */
	public void removeDevCard(DevCard card) {
		cards.remove(card);
	}
	
	/**
	 * randomly gets a card from the deck
	 * @return
	 */
	public DevCard getRandomCard() {
		Random rand = new Random();
		int randIndex = rand.nextInt(cards.size());
		return cards.get(randIndex);
	}
	
	
	/**
	 * 
	 * @return the remaining size of the cards ArrayList
	 */
	public int getCardRemaining(){
		return cards.size();
	}
	
	/**
	 * Returns a read-only list of all the DevCards
	 * @return an unmodifiable List of DevCards
	 */
	public List<DevCard> getAllCards(){
		return Collections.unmodifiableList(cards);
	}
	
	/**
	 * Gets the count of cards in the deck of the given type
	 * @param type the Type of DevCard in question
	 * @return the count of DevCards of that type in the Deck
	 */
	public int getCountByType(DevCardType type){
		int output = 0;
		for (DevCard card : cards){
			if(card.getType() ==type){
				++output;
			}
		}
		return output;
	}

}
