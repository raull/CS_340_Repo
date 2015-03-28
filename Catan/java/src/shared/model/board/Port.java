package shared.model.board;

import java.util.ArrayList;

import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexLocation;


/**
 * This class represent a Port on a {@link Map}
 * @author Raul Lopez
 *
 */
public class Port {

	/**
	 * {@link PortType} representing the resource that the <code>Port</code> may provide
	 */
	private PortType type;
	
	/**
	 * An integer representing the offer rate for the resource that it provides, must be either 2 or 3
	 */
	private int offerRate;
	
	private EdgeLocation edgeLocation;
	
	/**
	 * array of 2 vertices of port locations
	 */
	private ArrayList<Vertex> locations = new ArrayList<Vertex>();
	
	//Constructors
	/**
	 * Port Constructor for type and offerRate.
	 * @param type {@link PortType} representing the resource that the <code>Port</code>
	 * @param offerRate An integer representing the offer rate for the resource that it provides, must be either 2 or 3
	 */
	public Port(PortType type, int offerRate) {
		this.type = type;
		this.offerRate = offerRate;
	}
	
	public Port(PortType type, int offerRate, EdgeLocation edgeLoc)
	{
		this.type = type;
		this.offerRate = offerRate;
		this.edgeLocation = edgeLoc;
	}
	
	//Getters
	public PortType getType() {
		return type;
	}
	public int getOfferRate() {
		return offerRate;
	}

	public EdgeLocation getEdgeLocation() {
		return edgeLocation;
	}

	public void setEdgeLocation(EdgeLocation edgeLocation) {
		this.edgeLocation = edgeLocation;
	}

	public ArrayList<Vertex> getLocations() {
		return locations;
	}
	
	public void setLocations(ArrayList<Vertex> locations) {
		this.locations = locations;
	}
	
	public ResourceType getPortResourceType() {
		ResourceType resourceType = null;
		
		if(this.type == PortType.WOOD) {
			resourceType = ResourceType.WOOD;
		}
		else if(this.type == PortType.BRICK) {
			resourceType = ResourceType.BRICK;
		}
		else if(this.type == PortType.SHEEP) {
			resourceType = ResourceType.SHEEP;
		}
		else if(this.type == PortType.WHEAT) {
			resourceType = ResourceType.WHEAT;
		}
		else if(this.type == PortType.ORE) {
			resourceType = ResourceType.ORE;
		}
		
		return resourceType; 
	}
	
}
