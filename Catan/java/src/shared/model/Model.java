package shared.model;

import java.util.ArrayList;

import shared.definitions.CatanColor;
import shared.definitions.DevCardType;
import shared.definitions.HexType;
import shared.definitions.PieceType;
import shared.definitions.PortType;
import shared.definitions.ResourceType;
import shared.locations.EdgeDirection;
import shared.locations.EdgeLocation;
import shared.locations.HexLocation;
import shared.locations.VertexDirection;
import shared.locations.VertexLocation;
import shared.model.board.Edge;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.board.Port;
import shared.model.board.Vertex;
import shared.model.board.piece.Building;
import shared.model.board.piece.Piece;
import shared.model.board.piece.Road;
import shared.model.cards.Bank;
import shared.model.cards.DevCard;
import shared.model.cards.DevCardDeck;
import shared.model.cards.ResourceCard;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.MessageLine;
import shared.model.game.MessageList;
import shared.model.game.ScoreKeeper;
import shared.model.game.TradeOffer;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Model {
	
	//mostly according to the client model json spec
	public Bank bank; //cards available to be distributed to the players
	public MessageList chat; //all chat messages
	public MessageList log; //all log messages
	public Map map; //game map
	public TradeOffer tradeOffer; //current trade offer, if there is one
	public TurnManager turnManager; //the turntracker -- tracks who's turn it is
	public int version; //version of the model, to see if up to date
	public int winner; //-1 when nobody won yet. when someone wins, it's their order index
	public ScoreKeeper scoreKeeper;
	
	public Model(){
		this.bank = new Bank(new ArrayList<DevCard>(), new ArrayList<ResourceCard>());
		this.chat = new MessageList();
		this.log = new MessageList();
		this.map = new Map(new ArrayList<HexTile>());
		this.tradeOffer = null;
		this.turnManager = new TurnManager(new ArrayList<User>());
		this.version = 0;
		this.winner = -1;
		this.scoreKeeper = null; //initialize score keeper later
	}
	
	public Model(Bank bank, MessageList chat, MessageList log,
			Map map, TradeOffer tradeOffer,
			TurnManager turnManager, int version, int winner) {
		super();
		this.bank = bank;
		this.chat = chat;
		this.log = log;
		this.map = map;
		this.tradeOffer = tradeOffer;
		this.turnManager = turnManager;
		this.version = version;
		this.winner = winner;
	}

	public Bank getBank() {
		return bank;
	}

	public MessageList getChat() {
		return chat;
	}

	public MessageList getLog() {
		return log;
	}

	public Map getMap() {
		return map;
	}

	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}

	public TurnManager getTurnManager() {
		return turnManager;
	}

	public int getVersion() {
		return version;
	}

	public int getWinner() {
		return winner;
	}

	public ScoreKeeper getScoreKeeper() {
		return scoreKeeper;
	}
	/**
	 * serializes the current model into a JSON object
	 * @return
	 */
	public JsonObject serialize() {
		return null;
	}
	
	
	/**
	 * deserialize the model from the JSON response
	 * @param jsonModel
	 */
	
	public void deserialize(JsonObject jsonModel) {
		//would model be better as jsonElement?
		if(jsonModel.isJsonObject()) {
			
			
			JsonObject jsonObject = jsonModel.getAsJsonObject();
			
			//get bank resource cards
			ArrayList<ResourceCard> bankResourceCards = extractResourceDeck(jsonObject);
			//get bank dev cards
			ArrayList<DevCard> bankDevCards = extractDevDeck(jsonObject);
			
			//update bank
			bank.setDevCardDeck(new DevCardDeck(bankDevCards));
			bank.setResourceCardDeck(new ResourceCardDeck(bankResourceCards));
			
			//chat
			JsonObject jsonChat = jsonModel.get("chat").getAsJsonObject(); 
			chat = extractMessageList(jsonChat);
			
			//log
			JsonObject jsonLog = jsonModel.get("log").getAsJsonObject();
			log = extractMessageList(jsonLog);
			
			JsonObject jsonMap = jsonObject.get("map").getAsJsonObject();
			
			//update map/hexes
			ArrayList<HexTile> mapHexTiles = extractHexes(jsonMap);
			map.setHexTiles(mapHexTiles);
		
			//get roads and update users
			map.setRoadsOnMap(extractRoads(jsonMap));
				
			//get cities
			map.setCitiesOnMap(extractBuildings(jsonMap, "cities", PieceType.CITY));
				
			//get settlements
			map.setSettlementsOnMap(extractBuildings(jsonMap, "settlements", PieceType.SETTLEMENT));
				
			//get ports
			map.setPortsOnMap(extractPorts(jsonMap));
			
			//extract and update users
			extractUsers(jsonObject);
			
			scoreKeeper = new ScoreKeeper(turnManager.getUsers().size());
			
			//place robber
			extractRobber(jsonMap);
			
			//trade offer -- may be null
			if(jsonObject.get("tradeOffer") != null) {
				tradeOffer = new TradeOffer(new ResourceCardDeck(), new ResourceCardDeck());
				JsonObject jsonTradeOffer = jsonObject.get("tradeOffer").getAsJsonObject();
				updateTradeOffer(jsonTradeOffer);
			}

			//turn manager
			JsonObject jsonTurnManager = jsonModel.get("turnTracker").getAsJsonObject();
			updateTurnManager(jsonTurnManager);
			
			//version
			version = jsonModel.get("version").getAsInt();
			
			//winner
			winner = jsonModel.get("winner").getAsInt();
			scoreKeeper.setWinner(winner);
			
		}
	}
	
	
	public ArrayList<ResourceCard> extractResourceDeck(JsonObject jsonModel) {
		ArrayList<ResourceCard> bankResourceCards = new ArrayList<ResourceCard>();
		
		JsonObject jsonDeck = jsonModel.get("bank").getAsJsonObject();
		
		populateResourceDeck(jsonDeck, bankResourceCards, null);
		
		return bankResourceCards;
	}
	
	public ArrayList<DevCard> extractDevDeck(JsonObject jsonModel) {
		
		ArrayList<DevCard> bankDevCards = new ArrayList<DevCard>();
		
		JsonObject jsonDeck = jsonModel.get("deck").getAsJsonObject();
		
		populateDevCardDeck(jsonDeck, bankDevCards);
		
		return bankDevCards;
	}
	
	public void populateDevCardDeck(JsonObject jsonDeck, ArrayList<DevCard> devCards) {
		
		int yopCount = jsonDeck.get("yearOfPlenty").getAsInt();
		int monopolyCount = jsonDeck.get("monopoly").getAsInt();
		int soldierCount = jsonDeck.get("soldier").getAsInt();
		int roadBuildingCount = jsonDeck.get("roadBuilding").getAsInt();
		int monumentCount = jsonDeck.get("monument").getAsInt();
		
		addDevCardsByNum(devCards, yopCount, DevCardType.YEAR_OF_PLENTY);
		addDevCardsByNum(devCards, monopolyCount, DevCardType.MONOPOLY);
		addDevCardsByNum(devCards, soldierCount, DevCardType.SOLDIER);
		addDevCardsByNum(devCards, roadBuildingCount, DevCardType.ROAD_BUILD);
		addDevCardsByNum(devCards, monumentCount, DevCardType.MONUMENT);
	}
	
	public void addDevCardsByNum(ArrayList<DevCard> deckToAdd, int numTimes, DevCardType cardType) {
			for(int i = 0; i < numTimes; i++) {
				deckToAdd.add(new DevCard(cardType));
			}
	}
	
	//for chat or log
	public MessageList extractMessageList(JsonObject jsonMessageList) {
		
		JsonArray jsonMessageLines = jsonMessageList.get("lines").getAsJsonArray();
		ArrayList <MessageLine> messageLines = new ArrayList<MessageLine>();
		
		for(JsonElement jsonEleMessageLine : jsonMessageLines) {
			JsonObject jsonMessageLine = jsonEleMessageLine.getAsJsonObject();
			String message = jsonMessageLine.get("message").getAsString();
			String source = jsonMessageLine.get("source").getAsString();
			
			messageLines.add(new MessageLine(message, source));
		}
		
		MessageList messageList = new MessageList(messageLines);
		
		return messageList;
	}
	
	public ArrayList<HexTile> extractHexes(JsonObject jsonMap) {
		ArrayList<HexTile> mapHexTiles = new ArrayList<HexTile>();
		
		JsonArray jsonHexArray = jsonMap.get("hexes").getAsJsonArray();
		
		//go through and extract each hex object
		for(int i = 0; i < jsonHexArray.size(); i++) {
			JsonObject jsonHexTile = jsonHexArray.get(i).getAsJsonObject();
			HexTile hexTile = extractHexTile(jsonHexTile);
			mapHexTiles.add(hexTile);
		}
		
		return mapHexTiles;
	}
	
	public HexTile extractHexTile(JsonObject jsonHexTile) {
		Gson gson = new Gson();
		
		int number;
		HexType hexType = gson.fromJson(jsonHexTile.get("resource"), HexType.class);
		if (hexType == null) {
			hexType = HexType.DESERT;
			number = -1;
		} else {
			number = jsonHexTile.get("number").getAsInt();
		}
		HexLocation hexLocation = gson.fromJson(jsonHexTile.get("location"), HexLocation.class);
		
		HexTile hexTile = new HexTile(hexType, hexLocation, number); //hex type, hex location, hex number
		
		return hexTile;
	}
	
	public ArrayList<Port> extractPorts(JsonObject jsonMap) {
		ArrayList<Port> portsOnMap = new ArrayList<Port>();
		
		JsonArray jsonPorts = jsonMap.get("ports").getAsJsonArray();
		
		for(JsonElement jsonElePort : jsonPorts) {
			Port port = extractPort(jsonElePort.getAsJsonObject());
			portsOnMap.add(port);
		}
		
		return portsOnMap;
	}
	
	public Port extractPort(JsonObject jsonPort) {
		
		Gson gson = new Gson();
		PortType portType;
		
		if(jsonPort.get("resource") != null) {
			portType = gson.fromJson(jsonPort.get("resource"), PortType.class);
			ResourceType resourceType = gson.fromJson(jsonPort.get("resource"), ResourceType.class);
		}
		else{
			portType = PortType.THREE;
			//no resource type, could be any
		}
		
		HexLocation portLocation = gson.fromJson(jsonPort.get("location"), HexLocation.class);

		EdgeDirection portDirection = gson.fromJson(jsonPort.get("direction"), EdgeDirection.class);


		EdgeLocation location = new EdgeLocation(portLocation, portDirection);
		
		//pass in edgelocation to get array list of vertex locations
		ArrayList<VertexLocation> vertexLocations = Map.getAdjacentVertices(location);
		
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();
		
		for(VertexLocation vloc : vertexLocations) {
			Vertex vertex = new Vertex(vloc);
			vertices.add(vertex);
		}
		
		int ratio = jsonPort.get("ratio").getAsInt();
		
		Port port = new Port(portType, ratio);
		port.setLocations(vertices);
		
		return port;
	}
	
	public ArrayList<Road> extractRoads(JsonObject jsonMap) {
		
		ArrayList<Road> roads = new ArrayList<Road>();

		JsonArray jsonRoads = jsonMap.get("roads").getAsJsonArray();

		for(JsonElement jsonEleRoad : jsonRoads) {
			Road road = extractRoad(jsonEleRoad.getAsJsonObject());
			roads.add(road);
		}
		
		return roads;
	}
	
	public Road extractRoad(JsonObject jsonRoad) {
		Gson gson = new Gson();
		
		int owner = jsonRoad.get("owner").getAsInt();
		
		JsonObject jsonLocation = jsonRoad.get("location").getAsJsonObject();
		int x = jsonLocation.get("x").getAsInt();
		int y = jsonLocation.get("y").getAsInt();
		HexLocation hexLocation = new HexLocation(x, y);
		EdgeDirection direction = gson.fromJson(jsonLocation.get("direction"), EdgeDirection.class);
		
		EdgeLocation location = new EdgeLocation(hexLocation, direction);
		//need to populate user's occupied vertices, etc
		Road road = new Road();
		
		road.setOwner(owner);
		Edge edge = new Edge(location);
		road.setEdge(edge);
		
		return road;
	}
	
	public ArrayList<Building> extractBuildings(JsonObject jsonMap, String buildingType, PieceType pieceBuildingType) {
		ArrayList<Building> buildings = new ArrayList<Building>();
		
		//building type is settlements or cities
		JsonArray jsonBuildings = jsonMap.get(buildingType).getAsJsonArray();
		
		for(JsonElement jsonEleBuilding : jsonBuildings) {
			Building building = extractBuilding(jsonEleBuilding.getAsJsonObject(), pieceBuildingType);
			buildings.add(building);
		}
		
		return buildings;
	}
	
	public Building extractBuilding(JsonObject jsonBuilding, PieceType buildingType) {
		Gson gson = new Gson();
		
		int owner = jsonBuilding.get("owner").getAsInt();
		
		JsonObject jsonLocation = jsonBuilding.get("location").getAsJsonObject();
		int x = jsonLocation.get("x").getAsInt();
		int y = jsonLocation.get("y").getAsInt();
		HexLocation hexLocation = new HexLocation(x, y);
		VertexDirection direction = gson.fromJson(jsonLocation.get("direction"), VertexDirection.class);
		
		VertexLocation location = new VertexLocation(hexLocation, direction);
		
		Building building = new Building();
		building.setOwner(owner);
		building.setType(buildingType);
		
		Vertex vertex = new Vertex(location);
		building.setVertex(vertex);
		
		return building;
	}
	
	public void extractRobber(JsonObject jsonMap) {
		
		JsonObject jsonLocation = jsonMap.get("robber").getAsJsonObject();
		int x = jsonLocation.get("x").getAsInt();
		int y = jsonLocation.get("y").getAsInt();
		HexLocation hexLocation = new HexLocation(x, y);
		
		HexTile robberTile = map.getHexTileByLocation(hexLocation);
		robberTile.setRobber(true);
	}
	
//	public ArrayList<User> extractUsers(JsonObject jsonModel) {
	public void extractUsers(JsonObject jsonModel){
//		ArrayList<User> currentPlayers = new ArrayList<User>(); //prob updating the users in turnmanager
		
		JsonArray jsonUserArray = jsonModel.get("players").getAsJsonArray();
		
		
//		for(JsonElement jsonEleUser : jsonUserArray) {
		for(int i = 0; i < jsonUserArray.size(); i++) {
			if(jsonUserArray.get(i).isJsonNull()) {
				continue;
			}
			JsonObject jsonUser = jsonUserArray.get(i).getAsJsonObject();

//			JsonObject jsonUser = jsonEleUser.getAsJsonObject();
//			User user = extractUser(jsonUser);
			updateUser(jsonUser);
		}
//		return currentPlayers;
	}
	
	
	public void updateUser(JsonObject jsonUser) {
		Gson gson = new Gson();
		
		int numCities = jsonUser.get("cities").getAsInt();
		CatanColor userColor = gson.fromJson(jsonUser.get("color"), CatanColor.class);
		//discarded -- whether or not user has already discarded cards this turn
		boolean discarded = jsonUser.get("discarded").getAsBoolean();
		
		int monumentsPlayed = jsonUser.get("monuments").getAsInt();
		
		String name = jsonUser.get("name").getAsString();
		
		ArrayList<DevCard> newDevCards = new ArrayList<DevCard>();
		populateDevCardDeck(jsonUser.get("newDevCards").getAsJsonObject(), newDevCards);
		DevCardDeck newDevCardDeck = new DevCardDeck(newDevCards);
		
		ArrayList<DevCard> oldDevCards = new ArrayList<DevCard>();
		populateDevCardDeck(jsonUser.get("oldDevCards").getAsJsonObject(), oldDevCards);
		DevCardDeck oldDevCardDeck = new DevCardDeck(oldDevCards);
		
		int playerIndex = jsonUser.get("playerIndex").getAsInt();
		
		boolean playedDevCard = jsonUser.get("playedDevCard").getAsBoolean();
		
		int playerID = jsonUser.get("playerID").getAsInt();
		
		ArrayList<ResourceCard> resourceCards = new ArrayList<ResourceCard>();
		populateResourceDeck(jsonUser.get("resources").getAsJsonObject(), resourceCards, null);
		ResourceCardDeck resourceDeck = new ResourceCardDeck(resourceCards);
		
		int roads = jsonUser.get("roads").getAsInt();
		
		int settlements = jsonUser.get("settlements").getAsInt();
		
		int soldiers = jsonUser.get("soldiers").getAsInt();
		
		int victoryPoints = jsonUser.get("victoryPoints").getAsInt();
		
		User currUser = turnManager.getUser(playerID);
		if (currUser == null) {
			currUser = new User();
			currUser.setPlayerID(playerID);
			try {
				turnManager.addUser(currUser);
			} catch (Exception e) {
				System.out.println("Too many players, cannot add a new one");
			}
		}
		
		currUser.setPlayerTurnIndex(playerIndex);
		
		currUser.setUnusedCities(numCities);
		currUser.setHasPlayedDevCard(playedDevCard);
//		currUser.setDiscarded(discarded); -- does it matter whether user has already discarded cards?
//		currUser.setMonuments(monumentsPlayed); -- does user need to keep track of how many monuments are played? maybe in scorekeeper?
		currUser.getHand().setNewDevCardDeck(newDevCardDeck);
		currUser.getHand().setDevCardDeck(oldDevCardDeck);
		currUser.getHand().setResourceCardDeck(resourceDeck);
		
		currUser.setUnusedRoads(roads);
		currUser.setUnusedSettlements(settlements);
		
		currUser.setSoldiers(soldiers);
		currUser.setVictoryPoints(victoryPoints);
		
		updateUserPieces(currUser);
		//color
		currUser.setColor(userColor);
		currUser.setName(name);
	}
	
	public void updateUserPieces(User user) {
		ArrayList<Road> roads = map.getRoadsOnMap();
		
		for(Road road : roads) {
			//user owns the road
			if(road.getOwner() == user.getTurnIndex()) {
				user.addOccupiedEdge(road.getEdge());
			}
		}
		
		ArrayList<Building> settlements = map.getSettlementsOnMap();
		
		for(Building settlement : settlements) {
			if(settlement.getOwner() == user.getTurnIndex()) {
				user.addOccupiedVertex(settlement.getVertex());
			}
		}
		
		ArrayList<Building> cities = map.getCitiesOnMap();
		
		for(Building city: cities) {
			if(city.getOwner() == user.getTurnIndex()) {
				user.addOccupiedVertex(city.getVertex());
			}
		}
		
		//user owns port if they have settlement or city on that location
		
		ArrayList<Port> ports = map.getPortsOnMap();
		
		for(Port port: ports) {
			for(Vertex portVertex : port.getLocations()) {
				if(user.occupiesVertex(portVertex.getLocation())) {
					user.addPort(port);
				}
			}
		}
	}
	
	//2nd array for when populating tradeOffer
	public void populateResourceDeck(JsonObject jsonDeck, ArrayList<ResourceCard> resourceCards1, ArrayList<ResourceCard> resourceCards2) {
			
		int brickCount = jsonDeck.get("brick").getAsInt();
		int oreCount = jsonDeck.get("ore").getAsInt();
		int sheepCount = jsonDeck.get("sheep").getAsInt();
		int wheatCount = jsonDeck.get("wheat").getAsInt();
		int woodCount = jsonDeck.get("wood").getAsInt();
		
		addToCards(brickCount, ResourceType.BRICK, resourceCards1, resourceCards2);
		addToCards(oreCount, ResourceType.ORE, resourceCards1, resourceCards2);
		addToCards(sheepCount, ResourceType.SHEEP, resourceCards1, resourceCards2);
		addToCards(wheatCount, ResourceType.WHEAT, resourceCards1, resourceCards2);
		addToCards(woodCount, ResourceType.WOOD, resourceCards1, resourceCards2);
		
	}
	
	public void addToCards(int count, ResourceType resourceType, ArrayList<ResourceCard> resourceCards1, ArrayList<ResourceCard> resourceCards2) {
		if(count < 0 && resourceCards2 != null) {
			addResourceCardsByNum(resourceCards2, -count, resourceType);
		}
		else{
			addResourceCardsByNum(resourceCards1, count, resourceType);
		}
	}
	
	public void addResourceCardsByNum(ArrayList<ResourceCard> deckToAdd, int numTimes, ResourceType cardType) {
			for(int i = 0; i < numTimes; i++) {
				deckToAdd.add(new ResourceCard(cardType));
			}
	}
	
	public void updateTradeOffer(JsonObject jsonTradeOffer) {
		int senderIndex = jsonTradeOffer.get("sender").getAsInt();
		int receiverIndex = jsonTradeOffer.get("receiver").getAsInt();
		
		//positives: resources being offered
		//negatives: resources being asked for
		
		ArrayList<ResourceCard> offeredCards = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> requestedCards = new ArrayList<ResourceCard>();
		
		populateResourceDeck(jsonTradeOffer.get("offer").getAsJsonObject(), offeredCards, requestedCards);
		
		tradeOffer.setReceiverIndex(receiverIndex);
		tradeOffer.setSenderIndex(senderIndex);
		tradeOffer.setReceivingDeck(new ResourceCardDeck(requestedCards));
		tradeOffer.setSendingDeck(new ResourceCardDeck(offeredCards));
		
	}
	
	public void updateTurnManager(JsonObject jsonTurnManager) {
		Gson gson = new Gson();
		
		int currentTurn = jsonTurnManager.get("currentTurn").getAsInt();
		
		TurnPhase currTurnPhase = gson.fromJson(jsonTurnManager.get("status"), TurnPhase.class);

		//index of user with longest road, -1 if none
		int longestRoad = jsonTurnManager.get("longestRoad").getAsInt();
		
		int largestArmy = jsonTurnManager.get("largestArmy").getAsInt();
		
		turnManager.setCurrentTurn(currentTurn);
		turnManager.setCurrentPhase(currTurnPhase);
		turnManager.setLongestRoadIndex(longestRoad);
		turnManager.setLargestArmyIndex(largestArmy);
		
	}
	
	public void updateScoreKeeper() {
		ArrayList<User> users = (ArrayList<User>) turnManager.getUsers();
		
		for(User user : users) {
			scoreKeeper.setScore(user.getTurnIndex(), user.getVictoryPoints());
		}
	}
	
}
