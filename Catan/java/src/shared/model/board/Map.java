package shared.model.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import shared.locations.HexLocation;
import shared.locations.VertexLocation;

public class Map {

	private ArrayList<HexTile> hexTiles;
	
	//Getters
	
	public Collection<HexTile> getHexTiles() {
		return Collections.unmodifiableCollection(this.hexTiles);
	}

	
	public Map(ArrayList<HexTile> hexTiles) {
		super();
		this.hexTiles = hexTiles;
	}

	/**
	 * Searches the available HexTile objects to see if any match the specified location. 
	 * @param location The location of the desired HexTile
	 * @return The HexTile, if found, else null
	 */
	public HexTile getHexTileByLocation(HexLocation location){
		for (HexTile hexTile : hexTiles){
			if(hexTile.getLocation().equals(location)){
				return hexTile;
			}
		}
		
		return null;
	}
	
	public void setHexTiles(ArrayList<HexTile> hexTiles) {
		this.hexTiles = hexTiles;
	}
}
