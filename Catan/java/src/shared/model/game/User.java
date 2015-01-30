package shared.model.game;

import java.awt.Color;
import java.util.ArrayList;

import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.model.board.Edge;
import shared.model.board.Vertex;
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
	private Hand hand;
	
	/**
	 * An ArrayList of {@link Edge}s representing the edges that the <code>User</code> occupies
	 */
	private ArrayList<Edge> occupiedEdges;
	
	/**
	 * An ArrayList of {@link Vertex}es representing the edges that the <code>User</code> occupies
	 */
	private ArrayList<Vertex> occupiedVertices;
	
	/**
	 * An int representing the unique ID of the player. Used in login/cookie functionality
	 */
	private int playerID;
	
	/**
	 * An int representing the index of the turn for the game
	 */
	private int turnIndex;
	
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
	
	//Getters
	
	public String getName() {
		return this.name.getName();
	}
	
	public Color getColor() {
		return color.getJavaColor();
	}
	
	public ArrayList<DevCard> getUsableDevCards() {
		return this.hand.getUsableDevCards().getAllCards();
	}
	
	public ArrayList<DevCard> getNewDevCards(){
		return this.hand.getNewDevCards().getAllCards();
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
		
		if (this.hand.getUsableDevCards().getCountByType(devCard.type) > 0) {
			return true;
		}
		
		return false;
	}
	
	//Getters
	
	public int getTurnIndex() {
		return turnIndex;
	}
		
	
}
