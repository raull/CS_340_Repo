package client.map;

import java.util.*;

import shared.definitions.*;
import shared.locations.*;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.board.Port;
import shared.model.board.piece.Building;
import shared.model.board.piece.Road;
import shared.model.facade.ModelFacade;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.proxy.ProxyException;
import shared.proxy.moves.BuildCity;
import client.base.*;
import client.data.*;
import client.manager.ClientManager;
import client.misc.MessageView;
import client.state.State;


/**
 * Implementation for the map controller
 */
public class MapController extends Controller implements IMapController, Observer {
	
	private IRobView robView;
	private MapControllerState state;
	private HexLocation robberLoc;
	
	//States
	private MapSetUpState setUpState = new MapSetUpState(this);
	private MapInactiveState inactiveState = new MapInactiveState(this);
	private MapPlayingState playingState = new MapPlayingState(this);
	private MapRoadBuildingState roadBuildingState = new MapRoadBuildingState(this);
	private MapRobbingState robbingState = new MapRobbingState(this);
	
	
	public MapController(IMapView view, IRobView robView) {
		
		super(view);
		
		setRobView(robView);
		
		ClientManager.instance().getModelFacade().addObserver(this);
	}
	
	public IMapView getView() 
	{
		return (IMapView)super.getView();
	}
	
	private IRobView getRobView() 
	{
		return robView;
	}
	private void setRobView(IRobView robView) 
	{
		this.robView = robView;
	}
	
	protected void initFromModel() 
	{
		if(ClientManager.instance().getCurrentGameInfo()==null){
			return;
		}
		//TODO add test so the map is only initialized once the specific game is joined
		//otherwise we are most likely going to get a whole bunch of null pointer exceptions
				
		//access the model 
		ModelFacade facade = ClientManager.instance().getModelFacade();
		Map map = facade.map();
		TurnManager turnManager = facade.turnManager();
		
		//Update Hex Tiles
		ArrayList<HexTile> hexes = new ArrayList<HexTile>(map.getHexTiles());
		for (HexTile hex : hexes)
		{
			HexLocation location = hex.getLocation();
			HexType type = hex.getType();
			int number = hex.getNumber();
			
			getView().addHex(location, type);
			if (number > 1) {
				getView().addNumber(location, number);
			}
			
			//also place the robber if necessary
			if (hex.hasRobber())
			{
				getView().placeRobber(location);
			}
		}
		
		//Hard code in the water tiles
		getView().addHex(new HexLocation(3,0), HexType.WATER);
		getView().addHex(new HexLocation(3,-1), HexType.WATER);
		getView().addHex(new HexLocation(3,-2), HexType.WATER);
		getView().addHex(new HexLocation(3,-3), HexType.WATER);
		getView().addHex(new HexLocation(2,1), HexType.WATER);
		getView().addHex(new HexLocation(2,-3), HexType.WATER);
		getView().addHex(new HexLocation(1,-3), HexType.WATER);
		getView().addHex(new HexLocation(0,-3), HexType.WATER);
		getView().addHex(new HexLocation(1,2), HexType.WATER);
		getView().addHex(new HexLocation(0,3), HexType.WATER);
		getView().addHex(new HexLocation(-1,-2), HexType.WATER);
		getView().addHex(new HexLocation(-1,3), HexType.WATER);
		getView().addHex(new HexLocation(-2,-1), HexType.WATER);
		getView().addHex(new HexLocation(-2,3), HexType.WATER);
		getView().addHex(new HexLocation(-3,0), HexType.WATER);
		getView().addHex(new HexLocation(-3,1), HexType.WATER);
		getView().addHex(new HexLocation(-3,2), HexType.WATER);
		getView().addHex(new HexLocation(-3,3), HexType.WATER);

		
		//Update Pieces
		updateRoads(turnManager, map);
		
		updateSettlements(turnManager, map);
					
		updateCities(turnManager, map);
		
		//Update Ports
		ArrayList<Port> ports = map.getPortsOnMap();
		for (Port port : ports)
		{
			EdgeLocation loc = port.getEdgeLocation();
			PortType type = port.getType();
			getView().addPort(loc, type);
		}
		
		determineState(turnManager);
		
		state.update();
	}

	public boolean canPlaceRoad(EdgeLocation edgeLoc) //done
	{
		return state.canPlaceRoad(edgeLoc);
	}

	public boolean canPlaceSettlement(VertexLocation vertLoc) //done
	{	
		return state.canPlaceSettlement(vertLoc);
	}

	public boolean canPlaceCity(VertexLocation vertLoc) //done
	{	
		return state.canPlaceCity(vertLoc);
	}

	public boolean canPlaceRobber(HexLocation hexLoc) //done
	{	
		return state.canPlaceRobber(hexLoc);
	}

	//This function is called when the user clicks on the map overlay to place a road
	//There are three different ways this could be called. 
	//first it could be called in the setup phase, in which case it is free and disconnected is allowed
	//second it could be called after the road is bought, in which case it is not free and must be connected
	//third it could be called after road building, in which case it is free but must be connected
	public void placeRoad(EdgeLocation edgeLoc) 
	{
		state.placeRoad(edgeLoc);
	}

	//This function is called when the user clicks on the map overlay to place a settlement.
	//first this could happen if the settlement is bought
	//second this could happen if the game is in the setup phase.
	public void placeSettlement(VertexLocation vertLoc)
	{
		state.placeSettlement(vertLoc);
	}

	//This method is called when a user clicks on the map overlay to place a city.
	//This will only happen when the turnphase is playing and the user has bought a city
	public void placeCity(VertexLocation vertLoc)
	{
		PlayerInfo client = ClientManager.instance().getCurrentPlayerInfo();
		getView().placeCity(vertLoc, client.getColor());
		BuildCity buildCity = new BuildCity(client.getPlayerIndex(), vertLoc);
		try {
			ClientManager.instance().getServerProxy().buildCity(buildCity);
			ClientManager.instance().forceUpdate();
		} catch (ProxyException e) {
			MessageView errorMessage = new MessageView();
			errorMessage.setTitle("Error");
			errorMessage.setMessage("Something wrong happened while trying to place the city. Please try again later.");
			errorMessage.showModal();
		}
	}

	//This method is called when a user clicks on the map overlay to place the robber
	//first this will be called after a soldier card is played
	//second this will be called after a 7 is rolled
	public void placeRobber(HexLocation hexLoc) 
	{
		getView().placeRobber(hexLoc);
		
		robberLoc = hexLoc;
		
		//set the robView with the potential robbing victims
		ModelFacade facade = ClientManager.instance().getModelFacade();
		
		//identify the hextile associated with the hexlocation
		HexTile tile = facade.getHexTileFromHexLoc(robberLoc);
		
		TurnManager turnManager = facade.turnManager();
		ArrayList<User> users = new ArrayList<User>(turnManager.getUsers());
		int clientIndex = ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex();
		User currentUser = turnManager.getUserFromIndex(clientIndex);
		
		ArrayList<RobPlayerInfo> victims = new ArrayList<RobPlayerInfo>();
		
		for (User victim : users)
		{
			if (facade.canRobPlayer(tile, currentUser, victim))
			{
				RobPlayerInfo victimInfo = new RobPlayerInfo();
				victimInfo.setName(victim.getName());
				victimInfo.setColor(victim.getCatanColor());
				victimInfo.setNumCards(victim.getHand().getResourceCards().getAllResourceCards().size());
				victimInfo.setPlayerIndex(victim.getTurnIndex());
				victimInfo.setId(victim.getPlayerID());
				victims.add(victimInfo);
			}
		}
		RobPlayerInfo[] candidateVictims = victims.toArray(new RobPlayerInfo[victims.size()]);
		
		getRobView().setPlayers(candidateVictims);
		getRobView().showModal();
	}
	
	public void startMove(PieceType pieceType, boolean isFree, boolean allowDisconnected) //TODO need to understand this function more. When is it called? 
	{	
		state.startMove(pieceType, isFree, allowDisconnected);
	}
	
	public void cancelMove() //TODO need to figure out what, if anything, this needs to do
	{
		setState(new MapPlayingState(this));
		ClientManager.instance().forceUpdate();
	}
	
	public void playSoldierCard() 
	{	
		startMove(PieceType.ROBBER, true, true);
	}
	
	public void playRoadBuildingCard() 
	{	
		setState(new MapRoadBuildingState(this));
		startMove(PieceType.ROAD, true, false);
	}
	
	public void robPlayer(RobPlayerInfo victim) 
	{	
		state.robPlayer(victim, robberLoc);
	}

	@Override
	public void update(Observable o, Object arg) //TODO verify that this is correct
	{
		System.out.println("current turn phase: " + ClientManager.instance().getCurrentTurnPhase());
		if (state == null && ClientManager.instance().hasGameStarted()) {
			initFromModel();
		} else if (state != null){
			TurnManager turnManager = ClientManager.instance().getModelFacade().turnManager();
			determineState(turnManager);
			initFromModel();
			state.update();
		}
	}

	public void setState(State state) 
	{
		this.state = (MapControllerState) state;
	}
	
	//Update methods
	
	public void updateRoads(TurnManager turnManager, Map map) {
		ArrayList<Road> roads = map.getRoadsOnMap();
		for (Road road : roads)
		{
			EdgeLocation loc = road.getEdge().getLocation();
			User owner = turnManager.getUserFromIndex(road.getOwner());
			CatanColor color = owner.getCatanColor();
			getView().placeRoad(loc, color);
		}
	}
	
	public void updateSettlements(TurnManager turnManager, Map map) {
		ArrayList<Building> settles = map.getSettlementsOnMap();
		for (Building settle : settles)
		{
			VertexLocation loc = settle.getVertex().getLocation();
			User owner = turnManager.getUserFromIndex(settle.getOwner());
			CatanColor color = owner.getCatanColor();
			getView().placeSettlement(loc, color);
		}
	}
	
	public void updateCities(TurnManager turnManager, Map map) {
		ArrayList<Building> cities = map.getCitiesOnMap();
		for (Building city : cities)
		{
			VertexLocation loc = city.getVertex().getLocation();
			User owner = turnManager.getUserFromIndex(city.getOwner());
			CatanColor color = owner.getCatanColor();
			getView().placeCity(loc, color);
		}
	}
	
	public void updateRobber(TurnManager turnManager, Map map) {
		//Update Hex Tiles
		ArrayList<HexTile> hexes = new ArrayList<HexTile>(map.getHexTiles());
		for (HexTile hex : hexes)
		{
			HexLocation location = hex.getLocation();
			//Update Robber
			if (hex.hasRobber())
			{
				getView().placeRobber(location);
			}
		}
	}
	
	public void determineState(TurnManager turnManager) {
		
		if (state instanceof MapRoadBuildingState)
		{
			return;
		}
		
		//set the state
		int clientIndex = ClientManager.instance().getCurrentPlayerInfo().getPlayerIndex();
		if (turnManager.getCurrentTurn() != clientIndex)
		{
			setState(inactiveState);
		}
		else if (turnManager.currentTurnPhase() == TurnPhase.ROBBING)
		{
			setState(robbingState);
		}
		else if (turnManager.currentTurnPhase() == TurnPhase.PLAYING)
		{
			setState(playingState);
		}
		else if (turnManager.currentTurnPhase() == TurnPhase.FIRSTROUND
				|| turnManager.currentTurnPhase() == TurnPhase.SECONDROUND)
		{
			setState(setUpState);
		}
		else
		{
			setState(inactiveState);
		}
	}
	
}

