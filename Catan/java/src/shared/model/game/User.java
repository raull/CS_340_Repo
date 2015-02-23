package shared.model.game;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.board.Edge;
import shared.model.board.Port;
import shared.model.board.Vertex;
import shared.model.board.piece.Piece;
import shared.model.cards.DevCard;
import shared.model.cards.Hand;
import shared.model.cards.ResourceCardDeck;

/**
 * This class represent a user for playing games and authenticating as well
 * @author Raul Lopez
 *
 */
public class User {

	/**
	 * Color that represents the <code>User</code> described by {@link CatanColor}
	 */
	private CatanColor color;
	/**
	 * A {@link PlayerName} representing the name of the <code>User</code>
	 */
	private PlayerName name;
	/**
	 * A {@link playerPassword} representing the password of the <code>User</code>
	 */
	private Password password;
	/**
	 * A {@link Hand} representing the cards of the <code>User</code>
	 */
	private Hand hand = new Hand();
	
	/**
	 * An ArrayList of {@link Edge}s representing the edges that the <code>User</code> occupies
	 */
	private ArrayList<Edge> occupiedEdges = new ArrayList<Edge>();
	
	/**
	 * An ArrayList of {@link Vertex}es representing the edges that the <code>User</code> occupies
	 */
	private ArrayList<Vertex> occupiedVertices = new ArrayList<Vertex>();
	
	/**
	 * An ArrayList of {@link Piece} representing the unplayed pieces of the user
	 */
	private ArrayList<Piece> unusedPieces;
	//instead of array list, maybe just have count?
	private int unusedRoads;
	private int unusedSettlements;
	private int unusedCities;
	
	/**
	 * An int representing the unique ID of the player. Used in login/cookie functionality
	 */
	private int playerID;
	
	/**
	 * An int representing the index of the turn for the game
	 */
	private int turnIndex;
	
	private boolean hasPlayedDevCard = false;
	
	//if user has already discarded this turn
	private boolean hasDiscarded = false;
	
	private int brickCards;
	private int oreCards;
	private int sheepCards;
	private int wheatCards;
	private int woodCards;
	
	private ArrayList<Port> ports = new ArrayList<Port>();
	
	int soldiers = 0;
	
	private int victoryPoints = 0;
	
	/**
	 * Constructor to instantiate a <code>User</code> with authentication information and a color
	 * @param A {@link String} representing the name of the <code>User</code>
	 * @param A {@link String} representing the password of the <code>User</code>
	 * @param A {@link CatanColor} representing the color of the <code>User</code>
	 */
	public User(String name, String password, CatanColor color) {
		this.name = new PlayerName(name);
		this.color = color;
	}
	
	public User() {
		
	}
	
	//Getters
	
	public String getName() {
		return this.name.getName();
	}
	
	public CatanColor getCatanColor() {
		
		return color;

	}
	
	public Color getColor() {
		return color.getJavaColor();
	}
	
	public ArrayList<DevCard> getUsableDevCards() {
		return new ArrayList<DevCard>( this.hand.getUsableDevCards().getAllCards());
	}
	
	public ArrayList<DevCard> getNewDevCards(){
		return new ArrayList<DevCard>( this.hand.getNewDevCards().getAllCards());
	}
	
	public ResourceCardDeck getResourceCards(){
		return this.hand.getResourceCards();
	}

	public String getPassword() {
		return password.getPassword();
	}
	
	public Hand getHand(){
		return hand;
	}
	
	public int getPlayerID() {
		return playerID;
	}

	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}

	/**
	 * simulates a user rolling the dice
	 * @return a random number 1-6
	 */
	public int rollDice(){
		return 0;
	}

	/**
	 * Determines whether a user can purchase a DevCard from the Bank
	 * @return true if the user has sufficient ResourceCards, else false
	 */
	public boolean canBuyDevCard(){
		
		if (TradeManager.hasEnoughResources(this.hand.getResourceCards(), TradeManager.priceForDevCard())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Determines whether a user can purchase a given Piece from the Bank
	 * @param type the desired type of Piece
	 * @return true if the user has sufficient ResourceCards, else false
	 */
	public boolean canBuyPiece(PieceType type){
		
		if (TradeManager.hasEnoughResources(this.hand.getResourceCards(), TradeManager.priceForPiece(type))) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Adds the given edge to the <code>User</code>'s inventory
	 */
	public void addOccupiedEdge(Edge edge){
		this.occupiedEdges.add(edge);
	}
	
	/**
	 * Adds the given vertex to the <code>User</code>'s inventory
	 */
	public void addOccupiedVertex(Vertex vertex){
		this.occupiedVertices.add(vertex);
	}
	
	/**
	 * Determines whether this player can play the given DevCard
	 * @param devCard the DevCard desired to be played
	 * @return true if the user owns the card and it can be played, else false
	 */
	public boolean canPlayDevCard(DevCard devCard){
		
		if ((this.hasPlayedDevCard && devCard.type != DevCardType.MONUMENT) || this.hand.getUsableDevCards().getCountByType(devCard.type) <= 0) {
			return false;
		}
		
		return true;
	}
	
	//Getters
	
	public int getTurnIndex() {
		return turnIndex;
	}
		
	/**
	 * Determines whether the user currently occupies a vertex with a given VertexLocation
	 * @param location the VertexLocation being searched for
	 * @return true if the User occupies the Vertex, false otherwise
	 */
	public boolean occupiesVertex(VertexLocation location){
		for (Vertex vertex : occupiedVertices){
			if(vertex.getLocation().getNormalizedLocation().equals(location.getNormalizedLocation())){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Determines whether the user currently occupies an Edge with a given EdgeLocation
	 * @param location the EdgeLocation being searched for
	 * @return true if the User occupies the Edge, false otherwise
	 */
	public boolean occupiesEdge(EdgeLocation location){
		for (Edge edge : occupiedEdges){
			if(edge.getLocation().getNormalizedLocation().equals(location.getNormalizedLocation())){
				return true;
			}
		}
		return false;
	}
	
	public boolean ownsAdjacentBuildingToHex(HexLocation location){
		ArrayList<VertexLocation> locations = new ArrayList<VertexLocation>();
		locations.add(new VertexLocation(location, VertexDirection.West));
		locations.add(new VertexLocation(location, VertexDirection.NorthWest));
		locations.add(new VertexLocation(location, VertexDirection.NorthEast));
		locations.add(new VertexLocation(location, VertexDirection.East));
		locations.add(new VertexLocation(location, VertexDirection.SouthEast));
		locations.add(new VertexLocation(location, VertexDirection.SouthWest));
		for(VertexLocation vl : locations){
			if(this.occupiesVertex(vl)){
				return true;
			}
		}
		return false;

	}
	
	/**
	 * Return all the ports related to the user
	 * @return A Collection of ports
	 */
	public Collection<Port> ports() {
		
		ArrayList<Port> ports = new ArrayList<Port>();
		
		for (Vertex vertex : occupiedVertices) {
			if (vertex.getPort() != null) {
				ports.add(vertex.getPort());
			}
		}
		
		return ports;
	}

	public void addPort(Port port) {
		ports.add(port);
	}
	
	public Boolean getHasPlayedDevCard() {
		return hasPlayedDevCard;
	}

	public void setHasPlayedDevCard(Boolean hasPlayedDevCard) {
		this.hasPlayedDevCard = hasPlayedDevCard;
	}
	
	
	public ArrayList<Piece> getUnusedPieces() {
		return unusedPieces;
	}

	public void setUnusedPieces(ArrayList<Piece> unusedPieces) {
		this.unusedPieces = unusedPieces;
	}
	
	public void setPlayerTurnIndex(int turnIndex) {
		this.turnIndex = turnIndex;
	}

	public int getUnusedRoads() {
		return unusedRoads;
	}

	public void setUnusedRoads(int unusedRoads) {
		this.unusedRoads = unusedRoads;
	}

	public int getUnusedSettlements() {
		return unusedSettlements;
	}

	public void setUnusedSettlements(int unusedSettlements) {
		this.unusedSettlements = unusedSettlements;
	}

	public int getUnusedCities() {
		return unusedCities;
	}

	public void setUnusedCities(int unusedCities) {
		this.unusedCities = unusedCities;
	}

	public void setColor(CatanColor color) {
		this.color = color;
	}
	
	public void setName(String name)
	{
		this.name = new PlayerName(name);
	}

	public boolean getHasDiscarded() {
		return hasDiscarded;
	}

	public void setHasDiscarded(boolean hasDiscarded) {
		this.hasDiscarded = hasDiscarded;
	}

	public int getBrickCards() {
		return brickCards;
	}

	public void setBrickCards(int brickCards) {
		this.brickCards = brickCards;
	}

	public int getOreCards() {
		return oreCards;
	}

	public void setOreCards(int oreCards) {
		this.oreCards = oreCards;
	}

	public int getSheepCards() {
		return sheepCards;
	}

	public void setSheepCards(int sheepCards) {
		this.sheepCards = sheepCards;
	}

	public int getWheatCards() {
		return wheatCards;
	}

	public void setWheatCards(int wheatCards) {
		this.wheatCards = wheatCards;
	}

	public int getWoodCards() {
		return woodCards;
	}

	public void setWoodCards(int woodCards) {
		this.woodCards = woodCards;
	}

	/**
	 * Check if user has a port with the specified port type
	 * @param portType The port type to check on the user
	 * @return True if the user contains a port with the same port type. False, otherwise.
	 */
	public boolean hasPort(PortType portType) {
				
		for (Port port : ports) {
			if (port.getType() == portType) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public boolean hasPortByResource(ResourceType resourceType) {
		for(Port port : ports) {
			if(port.getPortResourceType() == resourceType) {
				return true;
			}
		}
		return false;
	}

	
	public int getSoldiers() {
		return soldiers;
	}

	public void setSoldiers(int soldiers) {
		this.soldiers = soldiers;
	}

	public int getVictoryPoints() {
		return victoryPoints;
	}

	public void setVictoryPoints(int victoryPoints) {
		this.victoryPoints = victoryPoints;
	}
	
	
}
