package shared.model.board.piece;

import shared.definitions.PieceType;

/**A Piece class to represent a piece on the map either as a {@link Road} or as a {@link Building} by polymorphism
 * 
 * @author Raul Lopez
 *
 */
public abstract class Piece {
	
	private int owner;
	/**
	 * Get the type of the <code>Piece</code>
	 * @return
	 */
	public abstract PieceType getType();

	public int getOwner() {
		return owner;
	}
	public void setOwner(int owner) {
		this.owner = owner;
	}
	
}
