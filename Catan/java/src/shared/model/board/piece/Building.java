package shared.model.board.piece;

import shared.definitions.PieceType;
import shared.model.board.Vertex;

/**
 * A <code>Building</code> class that represents a piece that could be on a {@link Vertex}
 * @author Raul Lopez
 *
 */
public class Building extends Piece{
	
	private PieceType type;
	/**
	 * {@link Vertex} that the <code>Building</code> belongs to.
	 */
	private Vertex vertex;
	
	@Override
	public PieceType getType() {
		// TODO Auto-generated method stub
		return type;
	}

	public Vertex getVertex() {
		return vertex;
	}

	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}
	
	
}
