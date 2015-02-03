package shared.model.facade;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
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

public class ModelFacade {
	//canDo functions
	//get pieces from models
	//will eventually have to talk to controllers
	private Proxy proxy; //has a pointer to the server proxy to see if methods that user has called is valid
	//need separate "do" functions, canDos return booleans
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
	 * Determines if a user can discard cards 
	 * @param turnManager
	 * @param user - current user
	 * @param cardsToRemove - array list of cards to be discarded
	 * @return
	 */
	public Boolean canDiscardCards(TurnManager turnManager, User user, ArrayList<ResourceCard> cardsToRemove) {
		//if it isn't the user's turn and the status of model is not discarding
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.DISCARDING){
			return false;
		}
		ArrayList<ResourceCard> userCards = user.getHand().getResourceCards().getAllResourceCards();
		
		//if user doesn't have over 7 cards
		if(!(userCards.size() > 7)) {
			return false;
		}
		
		//if user doesn't have all the cards they are choosing to remove
		if(!userCards.containsAll(cardsToRemove)) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * determine if user can roll dice
	 * @param turnManager
	 * @param user
	 * @return
	 */
	public Boolean canRollNumber(TurnManager turnManager, User user) {
		//if it isn't the user's turn or the model isn't in the rolling status
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.ROLLING) {
			return false;
		}
		else {
			return true;
		}
	}
	
	/**
	 * Determines if user can buy a piece (road, settlement, city)
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @return boolean, whether user can buy a piece
	 */
	public Boolean canBuyPiece(TurnManager turnManager, User user, PieceType pieceType) {
		//if it's not user's turn or if user can't buy piece
		if(user != turnManager.currentUser() || !user.canBuyPiece(pieceType)) {
			return false;
		}
		else{
			return true;
		}
	}
	
//	/**
//	 * If user can place a road at location
//	 * @param turnManager if it is user's turn
//	 * @param location if the location is valid
//	 * @param user if user has a road 
//	 * @return
//	 */
//	
//	public Boolean canPlaceRoadAtLoc(TurnManager turnManager, Edge location, User user) {
//		//if it's not user's turn and the edge is already occupied, return false
//		if(user != turnManager.currentUser() || location.isOccupiedByRoad()) {
//			return false;
//		}
//		//check that user has a road/building connecting to new location
//		//suggestion for implementing hasAdjoiningPiece...perhaps have an array of edges/vertex that the user occupies?
//			//then given edge or vertex, just compare the ones near it with user's occupied ones
//			//or, have each edge stores what user occupies it, if at all, etc
//		if(!location.hasAdjoiningPiece(user)){
//			return false;
//		}
//		return true;
//	}
	
	/**
	 * if user can build a road at given location
	 * @param turnManager -- if it is user's turn and if model is at 'Playing'
	 * @param location -- where the road will be placed
	 * @param user
	 * @param free -- if it is set up round
	 * @return
	 */
	public Boolean canBuildRoad(TurnManager turnManager, Edge location, User user, boolean free) {
		//if it isn't user's turn or if model status is not on playing
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
			return false;
		}
		//if road location is not open
		if(location.isOccupiedByRoad()) {
			return false;
		}
		if(free) { //is set up round, user given road for free
			//road must be placed by settlement owned by player with no adjacent road
			
		}
		else{
			//if user doesn't have the required resources to buy a road
			if(!user.canBuyPiece(PieceType.ROAD)) {
				return false;
			}
			//if the location doesn't have another road owned by the player
			if(!location.hasAdjoiningPiece(user)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * if user can build a settlement at given location
	 * @param turnManager -- if it is user's turn, and phase is on playing
	 * @param location -- if location is valid
	 * @param user 
	 * @param free -- if it is set up round
	 * @return
	 */
	public Boolean canBuildSettlement(TurnManager turnManager, Vertex location, User user, boolean free) {
		//if it isn't user's turn or if model status is not on playing
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
			return false;
		}
		//if settlement location isn't open
		if(location.isOccupied()) {
			return false;
		}
		//if settlement is placed adjacent to another settlement
//		if(location.) {
//			return false;
//		}
		if(free) { //if it is set up round, user given settlement for free
			//settlemnet doesn't have to be connected to road
		}
		else{ //not set up round
			//if user doesn't have resources to get a settlement
			if(!user.canBuyPiece(PieceType.SETTLEMENT)) {
				return false;
			}
			//settlement has to be connected to a road user already owns
			
		}
		
		return true;
	}
	
	public Boolean canBuildCity(TurnManager turnManager, Vertex location, User user) {
		//if it isn't user's turn or if model status is not on playing
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
			return false;
		}
		//if user does not already have a settlement at the location
		if(!user.occupiesVertex(location.getLocation())) { //currently only checking that user occupies the vertex, not necessarily if it's a settlement
			return false;
		}
		//if user doesn't have enough resources
		if(!user.canBuyPiece(PieceType.CITY)) {
			return false;
		}
		
		return true;
	}
	
//	/**
//	 * If user can place a building (settlement or city) at a certain location
//	 * @param turnManager if it is user's turn
//	 * @param location if the location is valid
//	 * @param user if user has building
//	 * @param PieceType type of building
//	 * @return
//	 */
//	public Boolean canPlaceBuildingAtLoc(TurnManager turnManager, Vertex location, User user, PieceType building) {
//		
//		if(user != turnManager.currentUser() || location.isOccupied()) {
//			return false;
//		}
//		//check if user has at least one adjoining road
//		
//		return null;
//	}
	
	/**
	 * If user can buy a dev card
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @param devCardDeck if there are dev cards to buy
	 * @return
	 */
	public Boolean canBuyDevCard(TurnManager turnManager, User user, DevCardDeck devCardDeck) {
		//if it's not user's turn, or if turn phase is not on playing, or dev card deck is empty, or if user canont buy dev card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || devCardDeck.getAllCards().size() == 0 || !user.canBuyDevCard()) {
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
	//may need to implement specific dev cards
	
	/**
	 * whether or not the current user can rob someone
	 * @param hexTile -- new robber location
	 * @param currUser
	 * @param victim -- the victim to be robbed
	 * @return
	 */
	public Boolean canRobPlayer(HexTile hexTile,User currUser, User victim) {
		//if it isn't user's turn or if model status is not on playing
		if(currUser != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
			return false;
		}
		//if new location already has robber, robber is being kept in the same location 
		if(hexTile.hasRobber()) {
			return false;
		}
		//if the victim getting robbed has no resource cards
		if(victim.getHand().getResourceCards().getAllResourceCards().size() == 0){
			return false;
		}
		
		return true;
	}
	
//	/**
//	 * If the robber can be placed/moved to a location
//	 * @param hexTile tile that the robber will be moved to
//	 * @return
//	 */
//	public Boolean canPlaceRobber(HexTile hexTile) {
//		if(hexTile.hasRobber()) {
//			return false;
//		}
//		else{
//			return true;
//		}
//	}
	
//	/**
//	 * if user can make a trade
//	 * @param turnManager
//	 * @param offeringUser - user making the offer
//	 * @param receivingUser - user accepting the offer
//	 * @param tradeOffer
//	 * @return
//	 */
//	//combines both offer and accept trade
//	public Boolean canOfferTrade(TurnManager turnManager, User offeringUser, User receivingUser, TradeOffer tradeOffer) {
//		if(offeringUser != turnManager.currentUser()) {
//			return false;
//		}
//		ArrayList<ResourceCard> offeringUserCards = offeringUser.getHand().getResourceCards().getAllResourceCards();
//		ArrayList<ResourceCard> offeredCards = tradeOffer.getBuyDeck().getAllResourceCards();
//		if(!offeringUserCards.containsAll(offeredCards)) {
//			return false;
//		}
//		ArrayList<ResourceCard> receivingUserCards = receivingUser.getHand().getResourceCards().getAllResourceCards();
//		ArrayList<ResourceCard> neededCards = tradeOffer.getSellDeck().getAllResourceCards();
//		if(!receivingUserCards.containsAll(neededCards)) {
//			return false;
//		}
//		return true;
//	}
	
	/**
	 * if user can make a trade
	 * @param turnManager - check that it is user's turn
	 * @param offeringUser - user making the offer
	 * @param receivingUser - user accepting the offer
	 * @param tradeOffer - contains details of offer
	 * @return
	 */
	public Boolean canOfferTrade(TurnManager turnManager, User offeringUser, User receivingUser, TradeOffer tradeOffer) {
		//if it isn't user's turn or if model status is not on playing
		if(offeringUser != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
			return false;
		}
		//if user does not have the resources they are offering
		ArrayList<ResourceCard> offeringUserCards = offeringUser.getHand().getResourceCards().getAllResourceCards(); //all of the offeringUser's cards
		ArrayList<ResourceCard> offeredCards = tradeOffer.getBuyDeck().getAllResourceCards(); //all of the cards offeringUser is offering
		if(!offeringUserCards.containsAll(offeredCards)) {
			return false;
		}
		return true;
	}
	
	/**
	 * if user can accept a trade
	 * @param user
	 * @param tradeOffer
	 * @return
	 */
	public Boolean canAcceptTrade(User user, TradeOffer tradeOffer) {
		//user hasn't been offered a trade
		if(tradeOffer == null) {
			return false;
		}
		ArrayList<ResourceCard> userCards = user.getHand().getResourceCards().getAllResourceCards();
		ArrayList<ResourceCard> neededCards = tradeOffer.getSellDeck().getAllResourceCards();
		//if user doesn't have all the required resources to accept offered trade
		if(!userCards.containsAll(neededCards)) {
			return false;
		}
		
		return true;
	}
	
	//helper function to check if user has a certain port type
	public Boolean checkUserHasPort(User user, PortType portType) {
		Collection<Port> ports = user.ports();
		for(Port port : ports) {
			if(port.getType().equals(portType)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * if user can maritime trade
	 * @param bank - if bank has resources
	 * @param user - if user has resources
	 * @param tradeOffer information about the offer (offeredCards, acceptedCards, ratio)
	 * @return
	 */
	public Boolean canMaritimeTrade(TurnManager turnManager, Bank bank, User user, TradeOffer tradeOffer) {
		//one ResourceCard per trade, assumes that the cards user is offering is all of the same type 
		if(user != turnManager.currentUser()) {
			return false;
		}
		//check if bank has cards user wants available
		ResourceCardDeck availableCards = bank.getResourceDeck(); //unimplemented function -- basically checks what cards bank has available
		ResourceCardDeck userCards = user.getHand().getResourceCards();
		ArrayList<ResourceCard> cardsWanted = tradeOffer.getBuyDeck().getAllResourceCards();
		ArrayList<ResourceCard> cardsOffered = tradeOffer.getSellDeck().getAllResourceCards();
		
		//if bank doesn't have all cards available, or if user doesn't have the cards they are offering
		if(!availableCards.getAllResourceCards().containsAll(cardsWanted) || !userCards.getAllResourceCards().containsAll(cardsOffered)) {
			return false;
		}
		
		int ratio = tradeOffer.getRate();
		ResourceType tradeType = cardsOffered.get(0).type;
		//if ratio is 4, default ok
		if(ratio == 4) {
			return true;
		}
		else if(ratio == 3) {
			//check that user has the THREE port
			if(!checkUserHasPort(user, PortType.THREE)) {
				return false;
			}
			else{
				return true;
			}
			
		}
		else if (ratio == 2){ 
			if(!checkUserHasPort(user, tradeType)) { //may have to convert resource type to port type?
				return false;
			}
			else {
				return true;
			}
		}
		else{
			return false; //ration shouldn't be anything besides 2-4 for maritime trade
		}
		
	}
	
	/**
	 * If user can end the turn
	 * @param turnManager check phase -- if all done, can end
	 * @param user -- user ending their turn
	 * @return
	 */
	public Boolean canFinishTurn(TurnManager turnManager, User user) {
		//if it isn't user's turn or if model status is not on playing
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
			return false;
		}
		return true;
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
