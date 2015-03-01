package shared.model.facade;

import java.util.ArrayList;
import java.util.Observable;

import client.manager.ClientManager;

import com.google.gson.JsonElement;

import shared.definitions.PieceType;
import shared.locations.EdgeDirection;
import shared.definitions.DevCardType;
import shared.definitions.HexType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.Model;
import shared.model.board.Edge;
import shared.model.board.HexTile;
import shared.model.board.Map;
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

public class ModelFacade extends Observable{
	

	//canDo functions
	//get pieces from models
	//will eventually have to talk to controllers
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
	
	private int modelVersion;
	private int winnerIndex;
	/**
	 * updates the model class with the JSON response
	 * @param jsonResponse
	 */
	public void updateModel(JsonElement jsonResponse) {
		model.deserialize(jsonResponse);
		turnManager = model.getTurnManager();
		map = model.getMap();
		bank = model.getBank();
		score = model.getScoreKeeper();

		winnerIndex = model.getWinner();
		//int newModelVersion = model.getVersion();


		
		System.out.println("Current State: " + turnManager.currentTurnPhase().toString());
		
		//check that version number has changed, or not
		/*if (ClientManager.instance().hasGameStarted()){
			if(modelVersion != newModelVersion) {
				//update stuff from model
				this.setChanged();
				this.notifyObservers();
			}
		}
		else{ */
			this.setChanged();
			this.notifyObservers();
		//}
		
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
		ResourceCardDeck userDeck = user.getHand().getResourceCards();
		ResourceCardDeck removeDeck = new ResourceCardDeck(cardsToRemove);
		
		//if user doesn't have over 7 cards
		if(!(userCards.size() > 7)) {
			return false;
		}
		
		//if user doesn't have all the cards they are choosing to remove
		if(!TradeManager.hasEnoughResources(userDeck, removeDeck)) {
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
	
//	public Boolean isEdgeOnWater(EdgeLocation location) {
//		HexLocation hexLoc = location.getHexLoc();
//		int radius = 3-1;
//		//bottom right of board, water edge on top, left
//		if(hexLoc.getX() > radius && hexLoc.getY() > radius) {
//			
//		}
//		//top right of board, water edge on bottom, left
//		else if(hexLoc.getX() > radius && hexLoc.getY() < radius) {
//			
//		}
//		//top left of board, water edge on bottom, right
//		else if(hexLoc.getX() < radius && hexLoc.getY() < radius){
//			
//		}
//		//bottom left of board, water edge on top, right
//		else if(hexLoc.getX() < radius && hexLoc.getY() > radius) {
//			
//		}
//		else{ //within radius, shouldn't be on water
//			return false;
//		}
//	}
//	
//	public Boolean isVertexOnWater(VertexLocation location) {
//		return false;
//	}
	
	/**
	 * Checks to see whether the location is valid for road placement - i.e. is it adjacent to other roads or buidlings
	 * owned by the user, is the location unoccupied, etc.
	 * @param turnManager -- if it is user's turn and if model is at 'Playing'
	 * @param location -- where the road will be placed
	 * @param user -- the user desiring to place the road
	 * @return true if the user can place the road at the given location, false otherwise
	 */
	
	public Boolean canPlaceRoadAtLoc(TurnManager turnManager, EdgeLocation location, User user) {
		location = location.getNormalizedLocation();
		
		//if it's not user's turn, return false
		if(user != turnManager.currentUser()) {
			return false;
		}
		
		//if trying to build road on water, return false
		HexLocation hexLocation1 = location.getHexLoc();
		HexLocation hexLocation2 = hexLocation1.getNeighborLoc(location.getDir());
		HexTile neighbor1 = map.getHexTileByLocation(hexLocation1);
		HexTile neighbor2 = map.getHexTileByLocation(hexLocation2);
		if(neighbor1==null && neighbor2 ==null){ //if both sides of the edge aren't valid Hexes
			return false;
		}
		
		
		//if edge is occupied, return false;
		ArrayList<User> users = new ArrayList<User>(turnManager.getUsers());
		for (User u : users){
			if(u.occupiesEdge(location)){
				return false;
			}
		}
		
		if (turnManager.currentTurnPhase() == TurnPhase.FIRSTROUND 
				|| turnManager.currentTurnPhase() == TurnPhase.SECONDROUND)
		{
			//user can't build a road during setup if no building can be attached to it
			ArrayList<VertexLocation> adjacent = Map.getAdjacentVertices(location);
			for(VertexLocation vl : adjacent){
				if(this.isValidBuildLocation(vl, user, PieceType.SETTLEMENT)){
					return true;
				}
			}
			return false;
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
	
	/**
	 * Does basic adjacency checks
	 * @param location
	 * @param user
	 * @param type
	 * @return
	 */
	private boolean isValidBuildLocation(VertexLocation location, User user, PieceType type) {
		location = location.getNormalizedLocation();
		
		if(!this.meetsBuildingConstraints(location, user, type)){
			return false;
		}
		else if(type.equals(PieceType.CITY)){
			return true; //if we're dealing with a city, we don't need to do adjacenty checking
		}
		//settlement cannot be placed adjacent to other buildings
		
		VertexLocation vLoc1 = null;
		VertexLocation vLoc2 = null;
		VertexLocation vLoc3 = null;

		if(location.getDir() == VertexDirection.NorthEast){
			vLoc1 = new VertexLocation(location.getHexLoc(), VertexDirection.East);
			vLoc2 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthWest);
			vLoc3 = new VertexLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast), VertexDirection.NorthWest);
		}
		else if(location.getDir() == VertexDirection.NorthWest){
			vLoc1 = new VertexLocation(location.getHexLoc(), VertexDirection.West);
			vLoc2 = new VertexLocation(location.getHexLoc(), VertexDirection.NorthEast);
			vLoc3 = new VertexLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest), VertexDirection.NorthEast);
		}
		else{
			assert(false); //means the normalization broke and all is lost
		}
		
		for(User u : turnManager.getUsers()){
			if(vLoc1==null || vLoc2==null || vLoc3==null){
				System.out.println("PROBLEM: In ModelFacade method isValidBuildingLoc, a vertexLocation was null (~line 334)");
			}
			if (u.occupiesVertex(vLoc1) || u.occupiesVertex(vLoc2) || u.occupiesVertex(vLoc3)){
				return false;
			}
		}

		
		return true;
	}
	
	/**
	 * Verifies that the individual piece constraints are met for building
	 * @param location
	 * @param user
	 * @param type
	 * @return
	 */
	private boolean meetsBuildingConstraints(VertexLocation location,
			User user, PieceType type) {

//		System.out.println("Entering meetsBuildingConstraints in ModelFacade");

		//checks for individual piece constrains
		if(type == PieceType.SETTLEMENT){
			//if the location is already occupied
			for(User u : turnManager.getUsers()){
				if(u.occupiesVertex(location)){
					return false;
				}
			}
			if (user.getUnusedSettlements() < 1)
			{
				return false;
			}
		}
		else if(type == PieceType.CITY){
			if (user.getUnusedCities() < 1)
			{
				return false;
			}
			
			//user must own a settlement at this location already
			if(!user.occupiesVertex(location)){
				return false;
			}
			else{
				return true;
			}
		}
		else{
			assert(false); //means the method was called incorrectly
		}
		return true;
	}
	public Boolean canPlaceRobberAtLoc(HexLocation hexLoc)
	{
		//account for water spaces?
		
		HexTile hex = map.getHexTileByLocation(hexLoc);
		
		if (hex == null) {
			return false;
		}
		
		if (hex.canMoveRobberHere())
		{
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
		location = location.getNormalizedLocation(); //restricts to NW and NE
		HexTile hex1 = map.getHexTileByLocation(location.getHexLoc()); 
		HexTile hex2 = map.getHexTileByLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.North));
		HexTile hex3;
		switch(location.getDir()){
			case NorthEast:
				hex3 = map.getHexTileByLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthEast));
				break;
			case NorthWest:
				hex3 = map.getHexTileByLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.NorthWest));
				break;
			default:
				assert false;
				return false;
			
		}
		if(hex1 == null && hex2 ==null && hex3 ==null){ //if all 3 are water(null), return false
			/*hex1 is the hex below our vertex, hex2 is the hex above, hex3*/
			return false;
		}
		
		//determines whether a road is already connected to here
		EdgeLocation edgeLoc1 = null;
		EdgeLocation edgeLoc2 = null;
		EdgeLocation edgeLoc3 = null;
		if(location.getDir()==VertexDirection.NorthEast){
			edgeLoc1 = new EdgeLocation(location.getHexLoc(), EdgeDirection.North);
			edgeLoc2 = new EdgeLocation(location.getHexLoc(), EdgeDirection.NorthEast);
			edgeLoc3 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.North), EdgeDirection.SouthEast);
		}
		else if(location.getDir()==VertexDirection.NorthWest){
			edgeLoc1 = new EdgeLocation(location.getHexLoc(), EdgeDirection.North);
			edgeLoc2 = new EdgeLocation(location.getHexLoc(), EdgeDirection.NorthWest);
			edgeLoc3 = new EdgeLocation(location.getHexLoc().getNeighborLoc(EdgeDirection.North), EdgeDirection.SouthWest);
		}
		if(!user.occupiesEdge(edgeLoc1) && !user.occupiesEdge(edgeLoc2) && !user.occupiesEdge(edgeLoc3)){
			return false; //returns false if user does not occupy any of the three locations
		}
		
		
		return this.isValidBuildLocation(location, user, type);
	}
	
	/**
	 * If user can buy a dev card
	 * @param turnManager if it is user's turn
	 * @param user get user and see if user has proper resources
	 * @param devCardDeck if there are dev cards to buy
	 * @return
	 */
	public Boolean canBuyDevCard(TurnManager turnManager, User user, DevCardDeck devCardDeck) {
		//if it's not user's turn, or if turn phase is not on playing, or dev card deck is empty, or if user cannot buy dev card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || devCardDeck.getAllCards().size() == 0 || !user.canBuyDevCard()) {
			return false;
		}
//		if(user != turnManager.currentUser()){
//			System.out.println("not current user");
//			return false;
//		}
//		else if(turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
//			System.out.println("not in correct turnphase");
//			return false;
//		}
//		else if(devCardDeck.getAllCards().size() == 0) {
//			System.out.println("dev card deck empty");
//			return false;
//		}
//		else if(!user.canBuyDevCard()) {
//			System.out.println("user can't buy dev card");
//			return false;
//		}
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
		if(currUser != turnManager.currentUser() 
				|| (turnManager.currentTurnPhase() != TurnPhase.PLAYING 
				&& turnManager.currentTurnPhase() != TurnPhase.ROBBING)) {
			return false;
		}
		//if new location already has robber, robber is being kept in the same location 
		if(hexTile.hasRobber()) {
			return false;
		}
		
		//the victim needs to be adjacent to the robber
		if(!victim.ownsAdjacentBuildingToHex(hexTile.getLocation())){
			return false;
		}
		
		//if the victim getting robbed has no resource cards
		if(victim.getHand().getResourceCards().getAllResourceCards().size() == 0){
			return false;
		}
		
		//can't steal from yourself
		if (currUser == victim)
		{
			return false;
		}
		
		return true;
	}
	
	public HexTile getHexTileFromHexLoc(HexLocation hexLoc)
	{
		return this.map().getHexTileByLocation(hexLoc);
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
		//if it isn't user's turn if model status is not on playing
		if (offeringUser != turnManager.currentUser()) {
			return false;
		}
		
		//If it's trading with itself
		if (offeringUser == receivingUser) {
			return false;
		}
		
		//if model status is not on playing
		if (turnManager.currentTurnPhase() != TurnPhase.PLAYING) {
			return false;
		}
		
		//If the offer can be made
		if(!TradeManager.canMakeOffer(offeringUser, receivingUser, tradeOffer)) {
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
	public Boolean canAcceptTrade(TurnManager turnManager, User user, TradeOffer tradeOffer) {
		//user hasn't been offered a trade
		if(tradeOffer == null) {
			return false;
		}
		
		//Check if the user is the one receiving the offer
		if (turnManager.getUserFromIndex(tradeOffer.getReceiverIndex()) != user) {
			return false;
		}
		
		ResourceCardDeck userCards = user.getHand().getResourceCards();
		ResourceCardDeck neededCards = tradeOffer.getReceivingDeck();
		//if user doesn't have all the required resources to accept offered trade
		if(!TradeManager.hasEnoughResources(userCards, neededCards)) {
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * if user can maritime trade
	 * @param turnManager checks if it's user's turn
	 * @param bank check if bank has the card wanted
	 * @param user who is trading
	 * @param wantedCard the card user wants
	 * @param offeredCards the cards user is giving
	 * @return
	 */
	public Boolean canMaritimeTrade(TurnManager turnManager, Bank bank, User user, ResourceCard wantedCard, ResourceCardDeck offeredCards) {
		//one ResourceCard per trade, users should only be able to offer cards all of the same type from UI
		if(user != turnManager.currentUser()) {
//			System.out.println("not user's turn");
			return false;
		}
		if(turnManager.currentTurnPhase() != TurnPhase.PLAYING){
//			System.out.println("wrong turn phase");
			return false;
		}
		
		ResourceCardDeck availableCards = bank.getResourceDeck();
		ResourceCardDeck userCards = user.getHand().getResourceCards();
		
		ArrayList<ResourceCard> wantedResourceCards = new ArrayList<ResourceCard>();
		wantedResourceCards.add(wantedCard);
		ResourceCardDeck wantedCardDeck = new ResourceCardDeck(wantedResourceCards);
		//if bank doesn't have all cards available
		if(!TradeManager.hasEnoughResources(availableCards, wantedCardDeck) ) {
//			System.out.println("bank doesn't have available cards");
			return false;
		}
		
		//user doesn't have cards they are offering
		if(!TradeManager.hasEnoughResources(userCards, offeredCards)) {
//			System.out.println("user doesn't have cards they are offering");
			return false;
		}
		
		//if user is trading the same resource for the same type -- why would you do this
		ResourceType userOfferedType = offeredCards.getAllResourceCards().get(0).getType();
		
		if(wantedCard.getType() == userOfferedType) {
			return false;
		}
		
		int ratio = offeredCards.getAllResourceCards().size(); //ratio of cards that the user is offering
		
		//check that the number of cards user is offering is possible due to a port they own
		//any maritime with 4 ratio is ok
		if(ratio == 4) {
			return true;
		}
		else if(ratio == 3) {
			if(!user.hasPort(PortType.THREE)) {
//				System.out.println("user trying to trade 3 for 1 without port");
				return false;
			}
//			System.out.println("user has a three port, ok");
			return true;
		}
		else if(ratio == 2) {
			if(!user.hasPortByResource(userOfferedType)){
//				System.out.println("user trying to trade 2 for 1 without specific port");
				return false;
			}
//			System.out.println("user has a specific port, ok");
			return true;
		}
		else{
			//shouldn't get here
			return false;
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
	public Boolean canPlaySoldier(TurnManager turnManager, User user, User victim, 
			HexTile newRobberLoc) {
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
		if(card1.getType()==card2.getType()){
			if(availableCards.getCountByType(card1.type)<2){ //if the cards are the same, make sure there's two of that type in the bank
				return false;
			}
		}
		else{
			if(availableCards.getCountByType(card1.type)<1||availableCards.getCountByType(card2.type)<1){ //else one of each type
				return false;
			}
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
	
	public Boolean canPlayRoadBuilding(TurnManager turnManager, User user) {
		DevCard roadCard = new DevCard(DevCardType.ROAD_BUILD);
		//if it isn't user's turn or if model status is not on playing or if user does not have road build card
		//if user has already played dev card
		if(user != turnManager.currentUser() || turnManager.currentTurnPhase() != TurnPhase.PLAYING || !user.canPlayDevCard(roadCard) || user.getHasPlayedDevCard()) {
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
	
	public int getWinnerIndex() {
		return winnerIndex;
	}
	public void setWinnerIndex(int winnerIndex) {
		this.winnerIndex = winnerIndex;
	}
	
	//getters/setters for model version
	public int getModelVersion() {
		return modelVersion;
	}
	
	public void setModelVersion(int modelVersion) {
		this.modelVersion = modelVersion;
	}
	
}
