package shared.model.facade;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.*;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;
import shared.model.Model;
import shared.model.board.Edge;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.board.Port;
import shared.model.board.Vertex;
import shared.model.cards.Bank;
import shared.model.cards.Card;
import shared.model.cards.DevCard;
import shared.model.cards.DevCardDeck;
import shared.model.cards.Hand;
import shared.model.cards.ResourceCard;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.ScoreKeeper;
import shared.model.game.TradeManager;
import shared.model.game.TradeOffer;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.proxy.Proxy;
import shared.proxy.ProxyException;
import shared.proxy.moves.*;

public class ModelFacade {
	//canDo functions
	//get pieces from models
	//will eventually have to talk to controllers
	private Proxy proxy; //has a pointer to the server proxy to see if methods that user has called is valid
	public Model model;
	
	/**
	 * The games's {@link ScoreKeeper} class to keep track of the score
	 */
	private ScoreKeeper score;
	/**
	 * The games's {@link TradeManager} class to keep track of all user's trading
	 */
	private TradeManager tradeManager;
	/**
	 * The games's {@link TurnManager} class to keep track of all user's turns
	 */
	private TurnManager turnManager;
	/**
	 * The games's {@link Map}
	 */
	private Map map;
	/**
	 * The games's {@link Bank} class to keep track of the game's decks
	 */
	private Bank bank;
	
	/**
	 * updates the model class with the JSON response
	 * @param jsonResponse
	 */
	public void updateModel(Model jsonResponse) {
		model.deserialize(jsonResponse);
	}
	/**
	 * gets the current model
	 * @return
	 */
	public Model getModel() {
		return model;
	}
	
	/**
	 * Determines if a user can discard cards (robber)
	 * @param turnManager
	 * @param user - current user
	 * @param cards - array list of cards to be discarded
	 * @return
	 */
	public Model canDiscardCards(TurnManager turnManager, User user, ArrayList<ResourceCard> cards) {
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.DISCARDING){
			return null;
		}
		Hand currHand = user.getHand();
		for(ResourceCard card : cards) {
			if(!currHand.canRemoveCard(card)) {
				return null;
			}
		}
		
		int brick = 0;
		int ore = 0;
		int sheep = 0;
		int wheat = 0;
		int wood = 0;
		
		for(ResourceCard card : cards) {
			switch(card.getType()) {
				case BRICK:
					brick++;
					break;
				case ORE:
					ore++;
					break;
				case SHEEP:
					sheep++;
					break;
				case WHEAT:
					wheat++;
					break;
				case WOOD:
					wood++;
					break;
			}
		}
		
		ResourceList resourceList = new ResourceList(brick, ore, sheep, wheat, wood);
		DiscardCards toDiscard = new DiscardCards(user.getPlayerID(), null);
		try {
			return proxy.discardCards(toDiscard);
		} catch (ProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * determine if user can roll dice
	 * @param turnManager
	 * @param user
	 * @return
	 */
	public Model canRollNumber(TurnManager turnManager, User user) {
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.ROLLING) {
			return null;
		}
		else {
			try {
				return proxy.rollNumber(new RollNumber(user.getPlayerID(), turnManager.getRolledNumber()));
			} catch (ProxyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * Determines if user can buy a piece (road, settlement, city)
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @return boolean, whether user can buy a piece
	 */
	public Boolean canBuyPiece(TurnManager turnManager, User user, PieceType pieceType) {
		if(user != turnManager.currentUser() || !user.canBuyPiece(pieceType)) {
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * If user can place a road at location
	 * @param turnManager if it is user's turn
	 * @param location if the location is valid
	 * @param user if user has a road 
	 * @return
	 */
	
	public Boolean canPlaceRoadAtLoc(TurnManager turnManager, Edge location, User user) {
		//if it's not user's turn and the edge is already occupied, return false
		if(user != turnManager.currentUser() || location.isOccupiedByRoad()) {
			return false;
		}
		//check that user has a road/building connecting to new location
		//suggestion for implementing hasAdjoiningPiece...perhaps have an array of edges/vertex that the user occupies?
			//then given edge or vertex, just compare the ones near it with user's occupied ones
			//or, have each edge stores what user occupies it, if at all, etc
		if(!location.hasAdjoiningPiece(user)){
			return false;
		}
		return true;
	}
	
	/**
	 * If user can place a building (settlement or city) at a certain location
	 * @param turnManager if it is user's turn
	 * @param location if the location is valid
	 * @param user if user has building
	 * @param PieceType type of building
	 * @return
	 */
	public Boolean canPlaceBuildingAtLoc(TurnManager turnManager, Vertex location, User user, PieceType building) {
		
		if(user != turnManager.currentUser() || location.isOccupied()) {
			return false;
		}
		//check if user has at least one adjoining road
		
		return null;
	}
	
	/**
	 * If user can buy a dev card
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @param devCardDeck if there are dev cards to buy
	 * @return
	 */
	public Boolean canBuyDevCard(TurnManager turnManager, User user, DevCardDeck devCardDeck) {
		//if it's not user's turn, or dev card deck is empty, or if user canont buy dev card
		if(user != turnManager.currentUser() || devCardDeck.getAllCards().size() == 0 || !user.canBuyDevCard()) {
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * If user can play the dev card
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has the card
	 * @return
	 */
	public Boolean canPlayDevCard(TurnManager turnManager, User user, DevCard devCard) {
		if(user != turnManager.currentUser() || !user.canPlayDevCard(devCard)) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * If the robber can be placed/moved to a location
	 * @param hexTile tile that the robber will be moved to
	 * @return
	 */
	public Boolean canPlaceRobber(HexTile hexTile) {
		if(hexTile.hasRobber()) {
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * if user can make a trade
	 * @param turnManager
	 * @param offeringUser - user making the offer
	 * @param receivingUser - user accepting the offer
	 * @param tradeOffer
	 * @return
	 */
	//combines both offer and accept trade
	public Boolean canOfferTrade(TurnManager turnManager, User offeringUser, User receivingUser, TradeOffer tradeOffer) {
		if(offeringUser != turnManager.currentUser()) {
			return false;
		}
		ArrayList<ResourceCard> offeringUserCards = offeringUser.getHand().getResourceCards().getAllResourceCards();
		ArrayList<ResourceCard> offeredCards = tradeOffer.getBuyDeck().getAllResourceCards();
		if(!offeringUserCards.containsAll(offeredCards)) {
			return false;
		}
		ArrayList<ResourceCard> receivingUserCards = receivingUser.getHand().getResourceCards().getAllResourceCards();
		ArrayList<ResourceCard> neededCards = tradeOffer.getSellDeck().getAllResourceCards();
		if(!receivingUserCards.containsAll(neededCards)) {
			return false;
		}
		return true;
	}
	
	/**
	 * if user can maritime trade
	 * @param bank - if bank has resources
	 * @param user - if user has resources
	 * @param tradeOffer information about the offer (offeredCards, acceptedCards)
	 * @return
	 */
	public Boolean canMaritimeTrade(TurnManager turnManager, Bank bank, User user,  TradeOffer tradeOffer){
		if(user != turnManager.currentUser()) {
			return false;
		}
		//check if bank has cards user wants available
		ResourceCardDeck availableCards = bank.getResourceDeck(); //unimplemented function -- basically checks what cards bank has available
		ResourceCardDeck userCards = user.getHand().getResourceCards();
		ArrayList<ResourceCard> cardsWanted = tradeOffer.getBuyDeck().getAllResourceCards();
		
		//this is assuming that tradeOffer has already calculated what is needed, etc
		ArrayList<ResourceCard> cardsOffered = tradeOffer.getSellDeck().getAllResourceCards();
		//if bank doesn't have all cards available, or if user doesn't have the cards they are offering
		if(!availableCards.getAllResourceCards().containsAll(cardsWanted) || !userCards.getAllResourceCards().containsAll(cardsOffered)) {
			return false;
		}
		
		//assuming that tradeOffer does not have rate calculated.. still need to finish implementing
//		ArrayList<ResourceCard> cardsNeeded = new ArrayList<ResourceCard>();
//		Collection<Port> userPorts = user.ports();
//		for(Port port : userPorts) {
//			for(ResourceCard card: cardsWanted) {
//				if(port.getType().equals(card.getType())) { //not sure if this will work because portType and cardType are different enums
//					int rate = port.getOfferRate();
//					//need rate number of cards for 1 wanted card
//				}
//			}
//		}
//		
		
		
		
		return true;
	}
	
	/**
	 * If user can end the turn
	 * @param turnManager check phase -- if all done, can end
	 * @return
	 */
	public Boolean canFinishTurn(TurnManager turnManager) {
		//not sure how to check for this..currently saying when turn phase is not anything, then user can end?
		if(turnManager.currentTurnPhase() != null) {
			return false;
		}
		else{
			return true;
		}
	}
	
	
	//Getters and Setters
	/**
	 * Get the {@link Bank} from the Game
	 * @return The Game's Bank
	 */
	public Bank bank() {
		return bank;
	}
	
	/**
	 * Get the {@link ScoreKeeper} from the Game
	 * @return The Game's ScoreKeeper
	 */
	public ScoreKeeper score() {
		return score;
	}
	
	/**
	 * Get the {@link TurnManager} from the Game
	 * @return The Game's TurnManager
	 */
	public TurnManager turnManager() {
		return turnManager;
	}
	
	/**
	 * Get the {@link Map} from the Game
	 * @return The Game's Map
	 */
	public Map map() {
		return map;
	}
	
	/**
	 * Get the {@link TradeManager} from the Game 
	 * @return The Game's TradeManager
	 */
	public TradeManager tradeManager() {
		return tradeManager;
	}
}
