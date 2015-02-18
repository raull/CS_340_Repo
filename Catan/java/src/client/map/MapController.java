package client.map;

import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import shared.model.board.HexTile;
import shared.model.board.Port;
import shared.model.board.piece.Building;
import shared.model.board.piece.Road;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.BuildRoad;
import shared.proxy.moves.Soldier_;
import client.base.*;
import client.data.*;
import client.manager.ClientManager;
import client.state.State;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {
	
	private IRobView robView;
	private MapControllerState state;
	
	public MapController(IMapView view, IRobView robView) {
		
		super(view);
		
		setRobView(robView);
		
		initFromModel();
	}
	
	public IMapView getView() {
		
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() {
		return robView;
	}
	private void setRobView(IRobView robView) {
		this.robView = robView;
	}
	
	protected void initFromModel() 
	{
		//access the model 
		ModelFacade facade = ClientManager.instance().getModelFacade();
		shared.model.board.Map map = facade.map();
		TurnManager turnManager = facade.turnManager();
		
		//getView().addHex for each hex in the model (hextype, number, location)
		ArrayList<HexTile> hexes = (ArrayList<HexTile>) map.getHexTiles();
		for (HexTile hex : hexes)
		{
			HexLocation location = hex.getLocation();
			HexType type = hex.getType();
			int number = hex.getNumber();
			
			getView().addHex(location, type);
			getView().addNumber(location, number);
		}
		
		//getView().placeRoad for each road
		ArrayList<Road> roads = map.getRoadsOnMap();
		for (Road road : roads)
		{
			EdgeLocation loc = road.getEdge().getLocation();
			User owner = turnManager.getUserFromIndex(road.getOwner());
			CatanColor color = owner.getCatanColor();
			getView().placeRoad(loc, color);
		}
		
		//getView().placeSettlement for each settlement
		ArrayList<Building> settles = map.getSettlementsOnMap();
		for (Building settle : settles)
		{
			VertexLocation loc = settle.getVertex().getLocation();
			User owner = turnManager.getUserFromIndex(settle.getOwner());
			CatanColor color = owner.getCatanColor();
			getView().placeSettlement(loc, color);
		}
					
		//getView().placeCity for each city
		ArrayList<Building> cities = map.getCitiesOnMap();
		for (Building city : cities)
		{
			VertexLocation loc = city.getVertex().getLocation();
			User owner = turnManager.getUserFromIndex(city.getOwner());
			CatanColor color = owner.getCatanColor();
			getView().placeCity(loc, color);
		}
		
		//identify where the robber is
		//getView().placeRobber(hexLoc);
		
		//getView().placePort for each port
		ArrayList<Port> ports = map.getPortsOnMap();
		for (Port port : ports)
		{
			//need to be able to acccess the edgelocation of the port
			EdgeLocation loc = new EdgeLocation(null, null); //port.getLocations().
			PortType type = port.getType();
			getView().addPort(loc, type);
		}
		
		//set the state
		User client = ClientManager.instance().getCurrentUser();
		if (turnManager.getCurrentTurn() != client.getTurnIndex())
		{
			setState(new MapInactiveState());
		}
		else if (turnManager.currentTurnPhase() == TurnPhase.ROBBING)
		{
			setState(new MapRobbingState());
		}
		else if (turnManager.currentTurnPhase() == TurnPhase.PLAYING)
		{
			setState(new MapPlayingState());
		}
		else if (turnManager.currentTurnPhase() == TurnPhase.FIRSTROUND
				|| turnManager.currentTurnPhase() == TurnPhase.SECONDROUND)
		{
			setState(new MapSetUpState());
		}
		else
		{
			setState(new MapInactiveState());
		}
		
		//<temp>
		
//		Random rand = new Random();
//
//		for (int x = 0; x <= 3; ++x) {
//			
//			int maxY = 3 - x;			
//			for (int y = -3; y <= maxY; ++y) {				
//				int r = rand.nextInt(HexType.values().length);
//				HexType hexType = HexType.values()[r];
//				HexLocation hexLoc = new HexLocation(x, y);
//				getView().addHex(hexLoc, hexType);
//				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
//						CatanColor.RED);
//				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
//						CatanColor.BLUE);
//				getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
//						CatanColor.ORANGE);
//				getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
//				getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);
//			}
//			
//			if (x != 0) {
//				int minY = x - 3;
//				for (int y = minY; y <= 3; ++y) {
//					int r = rand.nextInt(HexType.values().length);
//					HexType hexType = HexType.values()[r];
//					HexLocation hexLoc = new HexLocation(-x, y);
//					getView().addHex(hexLoc, hexType);
//					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.NorthWest),
//							CatanColor.RED);
//					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.SouthWest),
//							CatanColor.BLUE);
//					getView().placeRoad(new EdgeLocation(hexLoc, EdgeDirection.South),
//							CatanColor.ORANGE);
//					getView().placeSettlement(new VertexLocation(hexLoc,  VertexDirection.NorthWest), CatanColor.GREEN);
//					getView().placeCity(new VertexLocation(hexLoc,  VertexDirection.NorthEast), CatanColor.PURPLE);
//				}
//			}
//		}
//		
//		PortType portType = PortType.BRICK;
//		getView().addPort(new EdgeLocation(new HexLocation(0, 3), EdgeDirection.North), portType);
//		getView().addPort(new EdgeLocation(new HexLocation(0, -3), EdgeDirection.South), portType);
//		getView().addPort(new EdgeLocation(new HexLocation(-3, 3), EdgeDirection.NorthEast), portType);
//		getView().addPort(new EdgeLocation(new HexLocation(-3, 0), EdgeDirection.SouthEast), portType);
//		getView().addPort(new EdgeLocation(new HexLocation(3, -3), EdgeDirection.SouthWest), portType);
//		getView().addPort(new EdgeLocation(new HexLocation(3, 0), EdgeDirection.NorthWest), portType);
//		
//		getView().placeRobber(new HexLocation(0, 0));
//		
//		getView().addNumber(new HexLocation(-2, 0), 2);
//		getView().addNumber(new HexLocation(-2, 1), 3);
//		getView().addNumber(new HexLocation(-2, 2), 4);
//		getView().addNumber(new HexLocation(-1, 0), 5);
//		getView().addNumber(new HexLocation(-1, 1), 6);
//		getView().addNumber(new HexLocation(1, -1), 8);
//		getView().addNumber(new HexLocation(1, 0), 9);
//		getView().addNumber(new HexLocation(2, -2), 10);
//		getView().addNumber(new HexLocation(2, -1), 11);
//		getView().addNumber(new HexLocation(2, 0), 12);
//		
//		//</temp>
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) 
	{
		return state.canPlaceRoad(edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) 
	{	
		return state.canPlaceSettlement(vertLoc);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) {
		
		return state.canPlaceCity(vertLoc);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) {
		
		return state.canPlaceRobber(hexLoc);
	}

	public void placeRoad(EdgeLocation edgeLoc) 
	{
		
//		BuildRoad build = new BuildRoad(0, edgeLoc, false);
//		ClientManager.instance().getServerProxy().buildRoad();
	}

	public void placeSettlement(VertexLocation vertLoc) {
		
		getView().placeSettlement(vertLoc, CatanColor.ORANGE);
	}

	public void placeCity(VertexLocation vertLoc) {
		
		getView().placeCity(vertLoc, CatanColor.ORANGE);
	}

	public void placeRobber(HexLocation hexLoc) {
		
		getView().placeRobber(hexLoc);
		
		getRobView().showModal();
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) {	
		
		getView().startDrop(pieceType, CatanColor.ORANGE, true);
	}
	
	public void cancelMove() 
	{
		
	}
	
	public void playSoldierCard() 
	{	
		//initiate placement of the robber using map overlay and retrieve the new location
		//of the robber
		//do we then call place robber??
		//initiate the robview overlay to be able to get the index of the individual to be robbed
		//
	}
	
	public void playRoadBuildingCard() {	
		
	}
	
	public void robPlayer(RobPlayerInfo victim) 
	{	
		//states
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	public void setState(State state) {
		// TODO Auto-generated method stub
		
	}
	
}

