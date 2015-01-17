package shared.model.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class Map {

	private ArrayList<HexTile> hexTiles;
	
	//Getters
	
	public Collection<HexTile> getHexTiles() {
		return Collections.unmodifiableCollection(this.hexTiles);
	}
}
