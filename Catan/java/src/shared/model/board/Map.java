package shared.model.board;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import shared.locations.EdgeLocation;
import shared.locations.EdgeDirection;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
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
		roadsOnMap = new ArrayList<Road>();
		settlementsOnMap = new ArrayList<Building>();
		citiesOnMap = new ArrayList<Building>();
		portsOnMap = new ArrayList<Port>();
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
	
	public static ArrayList<VertexLocation> getAdjacentVertices(EdgeLocation location){
		ArrayList<VertexLocation> output = new ArrayList<VertexLocation>();
		location = location.getNormalizedLocation();
		switch(location.getDir()){
			case North:
				output.add(new VertexLocation(location.getHexLoc(), VertexDirection.NorthEast));
				output.add(new VertexLocation(location.getHexLoc(), VertexDirection.NorthWest));
				break;
			case NorthWest:
				output.add(new VertexLocation(location.getHexLoc(), VertexDirection.NorthWest));
				output.add(new VertexLocation(location.getHexLoc(), VertexDirection.West));
				break;
			case NorthEast:
				output.add(new VertexLocation(location.getHexLoc(), VertexDirection.NorthEast));
				output.add(new VertexLocation(location.getHexLoc(), VertexDirection.East));
				break;
			default:
				assert(false); //should never be reachable
				
		}
		return output;
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
	
	public void updateRobberLocation(HexLocation location)
	{
		for (HexTile hex : hexTiles)
		{
			if (hex.hasRobber())
			{
				hex.setRobber(false);
			}
		}
		
		HexTile hex = this.getHexTileByLocation(location);
		hex.setRobber(true);
	}
	
	public void addRoad(Road road) {
		this.roadsOnMap.add(road);
	}

	public void addCity(Building city){
		citiesOnMap.add(city);
	}
	
	public void addSettlement(Building settlement){
		settlementsOnMap.add(settlement);
	}
	
	public void removeSettlement(Building setllement){
		settlementsOnMap.remove(setllement);
	}
}
