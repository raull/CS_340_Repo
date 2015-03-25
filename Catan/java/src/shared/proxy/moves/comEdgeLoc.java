package shared.proxy.moves;

import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.VertexLocation;

public class comEdgeLoc {

	private int x;
	private int y;
	private EdgeDirection direction;
	
	//constructor
	public comEdgeLoc(EdgeLocation edge){
		x = edge.getHexLoc().getX();
		y = edge.getHexLoc().getY();
		
		direction = edge.getDir();
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public EdgeDirection getDirection() {
		return direction;
	}

	public void setDirection(EdgeDirection direction) {
		this.direction = direction;
	}
}
