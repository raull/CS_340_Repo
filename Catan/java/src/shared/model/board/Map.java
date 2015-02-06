package shared.model.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import shared.locations.HexLocation;
import shared.locations.VertexLocation;
import shared.model.board.piece.Building;
import shared.model.board.piece.Road;

public class Map {

	private ArrayList<HexTile> hexTiles;
	
	private ArrayList<Road> roadsOnMap;
	
	private ArrayList<Building> settlementsOnMap;
	
	private ArrayList<Building> citiesOnMap;
	
	private ArrayList<Port> portsOnMap;
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


	public ArrayList<Road> getRoadsOnMap() {
		return roadsOnMap;
	}


	public void setRoadsOnMap(ArrayList<Road> roadsOnMap) {
		this.roadsOnMap = roadsOnMap;
	}


	public ArrayList<Building> getSettlementsOnMap() {
		return settlementsOnMap;
	}


	public void setSettlementsOnMap(ArrayList<Building> settlementsOnMap) {
		this.settlementsOnMap = settlementsOnMap;
	}


	public ArrayList<Building> getCitiesOnMap() {
		return citiesOnMap;
	}


	public void setCitiesOnMap(ArrayList<Building> citiesOnMap) {
		this.citiesOnMap = citiesOnMap;
	}


	public ArrayList<Port> getPortsOnMap() {
		return portsOnMap;
	}


	public void setPortsOnMap(ArrayList<Port> portsOnMap) {
		this.portsOnMap = portsOnMap;
	}
	
	
}
