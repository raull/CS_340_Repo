package shared.proxy.moves;

import shared.locations.VertexLocation;

public class comVertexLoc {

	private int x;
	
	private int y;
	
	private String direction;
	
	public comVertexLoc(VertexLocation vertex){
		x = vertex.getHexLoc().getX();
		y = vertex.getHexLoc().getY();
		
		direction = vertex.getDir().original();
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

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}
}
