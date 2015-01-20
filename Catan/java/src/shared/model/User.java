package shared.model;

import java.awt.Color;
import java.util.ArrayList;

import shared.definitions.CatanColor;
import shared.definitions.PieceType;
import shared.model.board.piece.Piece;
import shared.model.cards.DevCard;
import shared.model.cards.DevCardDeck;
import shared.model.cards.Hand;
import shared.model.cards.ResourceCardDeck;
import shared.model.exception.InvalidMoveException;

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
	 * A {@link String} representing the name of the <code>User</code>
	 */
	private String name;
	/**
	 * A {@link String} representing the password of the <code>User</code>
	 */
	private String password;
	/**
	 * A {@link Hand} representing the cards of the <code>User</code>
	 */
	private Hand hand;
	private ArrayList<Piece> pieces;
	
	/**
	 * Constructor to instantiate a <code>User</code> with authentication information and a color
	 * @param A {@link String} representing the name of the <code>User</code>
	 * @param A {@link String} representing the password of the <code>User</code>
	 * @param A {@link CatanColor} representing the color of the <code>User</code>
	 */
	public User(String name, String password, CatanColor color) {
		this.name = name;
		this.color = color;
	}
	
	//Getters
	
	public String getName() {
		return this.name;
	}
	
	public Color getColor() {
		return color.getJavaColor();
	}
	
	public ArrayList<DevCard> getDevCards() {
		return this.hand.getDevCards().getAllCards();
	}
	
	public ResourceCardDeck getResourceCards(){
		return this.hand.getResourceCards();
	}

	public String getPassword() {
		return password;
	}
	
	public Hand getHand(){
		return hand;
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
		return false;
	}
	
	/**
	 * Notifies the bank that the user would like to purchase a DevCard
	 * @throws InvalidMoveException if the Bank is out of DevCards
	 */
	public void purchaseDevCard() throws InvalidMoveException{
		
	}
	
	/**
	 * Determines whether a user can purchase a given Piece from the Bank
	 * @param type the desired type of Piece
	 * @return true if the user has sufficient ResourceCards, else false
	 */
	public boolean canBuyPiece(PieceType type){
		return false;
	}
	
	/**
	 * Notifies the Bank that the user would like to purchase a Piece
	 * @throws InvalidMoveException if the piece cannot be purchased
	 */
	public void purchasePiece() throws InvalidMoveException{
		
	}
		
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	
}
