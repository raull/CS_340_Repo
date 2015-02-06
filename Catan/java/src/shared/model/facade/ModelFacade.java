package shared.model.facade;

import java.util.ArrayList;
import java.util.Collection;

import com.google.gson.JsonObject;

import shared.definitions.PieceType;
import shared.locations.EdgeDirection;
import shared.definitions.DevCardType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.Model;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.board.Port;
import shared.model.cards.Bank;
import shared.model.cards.DevCard;
import shared.model.cards.DevCardDeck;
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
	public Model model = new Model();
	
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
	public void updateModel(JsonObject jsonResponse) {
		model.deserialize(jsonResponse);
		turnManager = model.getTurnManager();
		map = model.getMap();
		bank = model.getBank();
		//update stuff from model
		
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
		ArrayList<ResourceCard> userCards = new ArrayList<ResourceCard>(user.getHand().getResourceCards().getAllResourceCards()) ;
		
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
		//if it's not user's turn, the user isn't in the right phase, or if user can't buy piece
		if(user != turnManager.currentUser() || !user.canBuyPiece(pieceType) || turnManager.currentTurnPhase()!= TurnPhase.PLAYING) {
			return false;
		}
		else{
			return true;
		}
	}
	
	/**
	 * Checks to see whether the location is valid for road placement - i.e. is it adjacent to other roads or buidlings
	 * owned by the user, is the location unoccupied, etc.
	 * @param turnManager -- if it is user's turn and if model is at 'Playing'
	 * @param location -- where the road will be placed
	 * @param user -- the user desiring to place the road
	 * @return true if the user can place the road at the given location, false otherwise
	 */
	
	public Boolean canPlaceRoadAtLoc(TurnManager turnManager, EdgeLocation location, User user) {
		//if it's not user's turn, return false
		if(user != turnManager.currentUser()) {
			return false;
		}
		
		//if trying to build road on water, return false
		
		//if edge is occupied, return false;
		ArrayList<User> users = new ArrayList<User>(turnManager.getUsers());
		for (User u : users){
			if(u.occupiesEdge(location)){
				return false;
			}
		}
		
		//check whether the user has a building connecting to new location
		location = location.getNormalizedLocation(); //standardizes address
		EdgeDirection direction = location.getDir();
		VertexLocation loc1 = null;
		VertexLocation loc2 = null;
		if(direction == EdgeDirection.North){
			loc1 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthEast);
			loc2 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthWest);
		}
		else if(direction == EdgeDirection.NorthEast){
			loc1 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthEast);
			loc2 = new VertexLocation(location.getHexLoc(), VertexDirection.East);
		}
		else if(direction == EdgeDirection.NorthWest){
			loc1 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthWest);
			loc2 = new VertexLocation(location.getHexLoc(), VertexDirection.West);
		}
		else{
			assert(false); //should never be reached
		}
		
		if(user.occupiesVertex(loc1) || user.occupiesVertex(loc2)){
			return true;
		}
		
		//check whether the user has a road connecting to new location
		EdgeLocation edgeLoc1 = null;
		EdgeLocation edgeLoc2 = null;
		EdgeLocation edgeLoc3 = null;
		EdgeLocation edgeLoc4 = null;
		if(direction == EdgeDirection.North){
			edgeLoc1 = new EdgeLocation(location.getHexLoc(), EdgeDirection.NorthEast);
			edgeLoc2 = new EdgeLocation(location.getHexLoc(), EdgeDirection.NorthWest);
			edgeLoc3 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.North), EdgeDirection.SouthEast);
			edgeLoc4 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.North), EdgeDirection.SouthWest);
		}
		else if(direction == EdgeDirection.NorthEast){
			edgeLoc1 = new EdgeLocation(location.getHexLoc(), EdgeDirection.North);
			edgeLoc2 = new EdgeLocation(location.getHexLoc(), EdgeDirection.SouthEast);
			edgeLoc3 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast), EdgeDirection.South);
			edgeLoc4 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast), EdgeDirection.NorthWest);
		}
		else if(direction == EdgeDirection.NorthWest){
			edgeLoc1 = new EdgeLocation(location.getHexLoc(), EdgeDirection.North);
			edgeLoc2 = new EdgeLocation(location.getHexLoc(), EdgeDirection.SouthWest);
			edgeLoc3 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest), EdgeDirection.South);
			edgeLoc4 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest), EdgeDirection.NorthEast);		
		}
		if(user.occupiesEdge(edgeLoc1) || user.occupiesEdge(edgeLoc2) || user.occupiesEdge(edgeLoc3) || user.occupiesEdge(edgeLoc4)){
			return true;
		}
		
		return false;
	}
	
	
	public Boolean canBuyRoadForLoc(TurnManager turnManager, EdgeLocation location, User user, boolean free){
		return (canBuyPiece(turnManager, user, PieceType.ROAD) && canPlaceRoadAtLoc(turnManager, location, user));
	}
	
	/**
	 * if user can build a settlement at given location
	 * @param turnManager -- if it is user's turn, and phase is on playing
	 * @param location -- if location is valid
	 * @param user 
	 * @param free -- if it is set up round
	 * @return
	 */
	public Boolean canPlaceBuildingAtLoc(TurnManager turnManager, VertexLocation location, User user, PieceType type) {
		//if it isn't user's turn
		if(user != turnManager.currentUser()) {
			return false;
		}
		
		//if it's not the right phase (either playing or setup rounds)
		if(!(turnManager.currentTurnPhase()==TurnPhase.PLAYING || 
				turnManager.currentTurnPhase() == TurnPhase.FIRSTROUND || turnManager.currentTurnPhase() == TurnPhase.SECONDROUND)){
			return false;
		}
		
		//if trying to build something on water, return false
		
		if(type == PieceType.SETTLEMENT){
			//if the location is already occupied
			for(User u : turnManager.getUsers()){
				if(u.occupiesVertex(location)){
					return false;
				}
			}
		}
		else if(type == PieceType.CITY){
			//user must own a settlement at this location already
			if(!user.occupiesVertex(location)){
				return false;
			}
		}
		else{
			assert(false); //means the method was called incorrectly
		}

		//building cannot be placed adjacent to other buildings
		location = location.getNormalizedLocation();
		VertexLocation vLoc1 = null;
		VertexLocation vLoc2 = null;
		VertexLocation vLoc3 = null;

		if(location.getDir() == VertexDirection.NorthEast){
			vLoc1 = new VertexLocation(location.getHexLoc(), VertexDirection.East);
			vLoc2 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthWest);
			vLoc3 = new VertexLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast), VertexDirection.West);
		}
		else if(location.getDir() == VertexDirection.NorthWest){
			vLoc1 = new VertexLocation(location.getHexLoc(), VertexDirection.West);
			vLoc2 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthEast);
			vLoc3 = new VertexLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest), VertexDirection.East);
		}
		else{
			assert(false); //means the normalization broke and all is lost
		}
		
		for(User u : turnManager.getUsers()){
			if (u.occupiesVertex(vLoc1) || u.occupiesVertex(vLoc2) || u.occupiesVertex(vLoc3)){
				return false;
			}
		}

		
		return true;
	}
	
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
		if(offeringUser != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || !TradeManager.canMakeOffer(offeringUser, receivingUser, tradeOffer)) {
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
		ResourceCardDeck userCards = user.getHand().getResourceCards();
		ResourceCardDeck neededCards = tradeOffer.getReceivingDeck();
		//if user doesn't have all the required resources to accept offered trade
		if(TradeManager.hasEnoughResources(userCards, neededCards)) {
			return false;
		}
		
		return true;
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
		
		//if bank doesn't have all cards available, or if user doesn't have the cards they are offering
		if(!TradeManager.hasEnoughResources(availableCards, tradeOffer.getReceivingDeck()) || !TradeManager.hasEnoughResources(userCards, tradeOffer.getSendingDeck())) {
			return false;
		}
		
		int ratio = tradeOffer.getRate();
		//if ratio is 4, default ok
		if(ratio == 4) {
			return true;
		}
		else if(ratio == 3) {
			//check that user has the THREE port
			if(!user.hasPort(PortType.THREE)) {
				return false;
			}
			else{
				return true;
			}
			
		}
		else if (ratio == 2){ 
			if(!user.hasPort(tradeOffer.getPortType())) { 
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
	
	//playing dev cards:
	//general pre conditions:
	//it is your turn
	//client model status is PLAYING
	//you have the specific card
	//you have not played a dev card this turn yet (excluding monument)
	
	/**
	 * if user can play soldier dev card
	 * soldier card moves the robber and allows user to rob someone
	 * @param turnManager -- tracks user's turns
	 * @param user -- user playing the dev card
	 * @param victim -- user that will be the victim of the dev card
	 * @param newRobberLoc -- dev card will move robber
	 * @return
	 */
	public Boolean canPlaySoldier(TurnManager turnManager, User user, User victim, HexTile newRobberLoc) {
		DevCard soldierCard = new DevCard(DevCardType.SOLDIER);
		//if it isn't user's turn or if model status is not on playing or if user does not have soldier card
		//if user has already played dev card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || !user.canPlayDevCard(soldierCard) || user.getHasPlayedDevCard()) {
			return false;
		}
		return canRobPlayer(newRobberLoc, user, victim);
	}
	
	/**
	 * if user can play year of plenty dev card 
	 * allows user to take two resource cards (any type) from bank
	 * @param turnManager
	 * @param user
	 * @param bank if bank has resources wanted available
	 * @param card1 the first wanted card
	 * @param card2 the second wanted card
	 * @return
	 */
	public Boolean canPlayYearofPlenty(TurnManager turnManager, User user, Bank bank, ResourceCard card1, ResourceCard card2) {
		DevCard yopCard = new DevCard(DevCardType.YEAR_OF_PLENTY);
		//if it isn't user's turn or if model status is not on playing or if user does not have year of plenty card
		//if user has already played dev card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || !user.canPlayDevCard(yopCard) || user.getHasPlayedDevCard()) {
			return false;
		}
		ResourceCardDeck availableCards = bank.getResourceDeck();
		//if bank  does not have resource cards wanted
		if(!availableCards.getAllResourceCards().contains(card1) || !availableCards.getAllResourceCards().contains(card2)) {
			return false;
		}
		return true;
	}
	
	/**
	 * if user can play road building dev card
	 * allows user to place two roads on the board as if they had just built them
	 * @param turnManager
	 * @param user
	 * @param spot1
	 * @param spot2
	 * @return
	 */
	
	public Boolean canPlayRoadBuilding(TurnManager turnManager, User user, EdgeLocation spot1, EdgeLocation spot2) {
		DevCard roadCard = new DevCard(DevCardType.ROAD_BUILD);
		//if it isn't user's turn or if model status is not on playing or if user does not have road build card
		//if user has already played dev card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || !user.canPlayDevCard(roadCard) || user.getHasPlayedDevCard()) {
			return false;
		}
		//if the location is not connected to an existing road/settlement owned by user
		if(canPlaceRoadAtLoc(turnManager, spot1, user) || canPlaceRoadAtLoc(turnManager, spot2, user)) {
			return false;
		}
		//if user does not have at least 2 un-used roads
		if(user.getUnusedRoads() < 2) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * if a user can play monopoly dev card
	 * takes all the cards of a certain resource from all players
	 * @param turnManager
	 * @param user
	 * @param resourceType the type of resource to be taken from other players
	 * @return
	 */
	public Boolean canPlayMonopoly(TurnManager turnManager, User user, ResourceType resourceType) {
		DevCard monopolyCard = new DevCard(DevCardType.MONOPOLY);
		//if it isn't user's turn or if model status is not on playing or if user does not have monopoly card
		//if user has already played dev card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || !user.canPlayDevCard(monopolyCard) || user.getHasPlayedDevCard()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * if user can play monument dev card
	 * @param turnManager
	 * @param user
	 * @return
	 */
	public Boolean canPlayMonument(TurnManager turnManager, User user) {
		DevCard monumentCard = new DevCard(DevCardType.MONUMENT);
		//if it isn't user's turn or if model status is not on playing or if user does not have monument card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || !user.canPlayDevCard(monumentCard)) {
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
