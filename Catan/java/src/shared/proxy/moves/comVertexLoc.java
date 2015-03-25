package shared.proxy.moves;

import shared.locations.VertexDirection;
import shared.locations.VertexLocation;

public class comVertexLoc {

	private int x;
	
	private int y;
	
	private VertexDirection direction;
	
	public comVertexLoc(VertexLocation vertex){
		x = vertex.getHexLoc().getX();
		y = vertex.getHexLoc().getY();
		
		direction = vertex.getDir();
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

	public VertexDirection getDirection() {
		return direction;
	}

	public void setDirection(VertexDirection direction) {
		this.direction = direction;
	}
}
