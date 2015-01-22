package shared.model.board.piece;

import shared.definitions.PieceType;
import shared.model.board.Edge;


/**A <code>Road</code> class that represents a piece that could be on an {@link Edge}
 * 
 * @author Raul Lopez
 *
 */
public class Road extends Piece{

	@Override
	public PieceType getType() {
		// TODO Auto-generated method stub
		return PieceType.ROAD;
	}

}
