package shared.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
import com.google.gson.JsonPrimitive;

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
	
	private boolean isUpdating = false;
	
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
	
	public boolean isUpdating() {
		return isUpdating;
	}

	public void setUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}
	
	/**
	 * serializes the current model into a JSON object
	 * @return
	 */
	public JsonElement serialize() {
		JsonObject serializedModel = new JsonObject();
		//first add the bank's resource list
		JsonElement bankJson = serializeResourceList(bank.getResourceDeck());
		serializedModel.add("bank", bankJson);
		//set the chat messages json
		JsonElement chatJson = serializeMessageList(chat);
		serializedModel.add("chat", chatJson);
		//set the game log messages json
		JsonElement logJson = serializeMessageList(log);
		serializedModel.add("log", logJson);
		//set the map json
		JsonElement mapJson = serializeMap();
		serializedModel.add("map", mapJson);
		//set the players json
		JsonElement usersJson = serializePlayers();
		serializedModel.add("players", usersJson);
		//set a trade offer if there is one
		if(this.tradeOffer != null) {
			//serialize the trade offer
			JsonElement tradeJson = serializeTradeOffer();
			serializedModel.add("tradeOffer", tradeJson);
		}
		//set the turn tracker
		JsonElement turnJson = serializeTurnTracker();
		serializedModel.add("turnTracker", turnJson);
		//set the model version number
		serializedModel.add("version", new JsonPrimitive(this.version));
		//set the winner
		serializedModel.add("winner", new JsonPrimitive(scoreKeeper.getWinner()));
		return serializedModel;
	}
	
	/**
	 * serialize a resource list
	 * @param resourceDeck 
	 * @return json resource list
	 */
	public JsonElement serializeResourceList(ResourceCardDeck resourceDeck) {
		JsonObject jsonResources = new JsonObject();
		
		jsonResources = new JsonObject();
		jsonResources.add("brick", new JsonPrimitive(resourceDeck.getCountByType(ResourceType.BRICK)));
		jsonResources.add("ore", new JsonPrimitive(resourceDeck.getCountByType(ResourceType.ORE)));
		jsonResources.add("sheep", new JsonPrimitive(resourceDeck.getCountByType(ResourceType.SHEEP)));
		jsonResources.add("wheat", new JsonPrimitive(resourceDeck.getCountByType(ResourceType.WHEAT)));
		jsonResources.add("wood", new JsonPrimitive(resourceDeck.getCountByType(ResourceType.WOOD)));
	
		
		return jsonResources;
	}
	
	/**
	 * serialize the chat or log message list
	 * @param messageList message list to be serialized
	 * @return json element
	 */
	public JsonElement serializeMessageList(MessageList messageList) {
		//message list is array of message lines
		ArrayList<MessageLine> lines = messageList.getLines();
		JsonArray jsonMessageLines = new JsonArray();
		for(MessageLine line : lines) {
			JsonObject jsonLine = new JsonObject();
			jsonLine.add("message", new JsonPrimitive(line.getMessage()));
			jsonLine.add("source", new JsonPrimitive(line.getSource()));
			jsonMessageLines.add(jsonLine);
		}
		return jsonMessageLines;
	}
	
	/**
	 * serialize the map 
	 * @return json element of map
	 */
	public JsonElement serializeMap() {
		JsonObject jsonMap = new JsonObject();
		//set array of hexes
		JsonElement jsonHexes = serializeMapHexes();
		jsonMap.add("hexes", jsonHexes);
		//set array of ports
		JsonElement jsonPorts = serializeMapPorts();
		jsonMap.add("ports", jsonPorts);
		//array of roads
		JsonElement jsonRoads = serializeMapRoads();
		jsonMap.add("roads", jsonRoads);
		//array of settlements
		JsonElement jsonSettlements = serializeBuildings(map.getSettlementsOnMap());
		jsonMap.add("settlements", jsonSettlements);
		//array of cities
		JsonElement jsonCities = serializeBuildings(map.getCitiesOnMap());
		jsonMap.add("cities", jsonCities);
		//radius is always 3 (and not really used?)
		//robber hex location
		for(HexTile hexTile : map.getHexTiles()) {
			//hex tile that has robber
			if(hexTile.hasRobber()) {
				JsonElement jsonLoc = serializeHexLocation(hexTile.getLocation());
				jsonMap.add("robber", jsonLoc);
				break;
			}
		}
		return jsonMap;
	}
	
	/**
	 * serialize the hex tiles on the map
	 * @return
	 */
	public JsonElement serializeMapHexes() {
		Collection<HexTile> hexTiles = map.getHexTiles();
		JsonArray jsonHexes = new JsonArray();
		for(HexTile hexTile: hexTiles) {
			JsonElement jsonHex = serializeMapHex(hexTile);
			jsonHexes.add(jsonHex);
		}
		return jsonHexes;
	}
	/**
	 * serialize the individual hex tile
	 * @return
	 */
	public JsonElement serializeMapHex(HexTile hexTile) {
		JsonObject jsonHex = new JsonObject();
		//add hex location
		JsonElement jsonHexLoc = serializeHexLocation(hexTile.getLocation());
		jsonHex.add("location", jsonHexLoc);
		//add the resource, if desert, don't add
		HexType resourceType = hexTile.getType();
		if(resourceType != HexType.DESERT) {
			jsonHex.add("resource", new JsonPrimitive(resourceType.name()));
		}
		//add the number on tile; if desert, don't add --water wouldn't have either
		if(resourceType != HexType.DESERT && resourceType != HexType.WATER) {
			jsonHex.add("number", new JsonPrimitive(hexTile.getNumber()));
		}
		
		return jsonHex;
	}
	
	/**
	 * serializes a hex location
	 * @param hexLoc
	 * @return json of hex location
	 */
	public JsonElement serializeHexLocation(HexLocation hexLoc) {
		JsonObject jsonHexLoc = new JsonObject();
		jsonHexLoc.add("x", new JsonPrimitive(hexLoc.getX()));
		jsonHexLoc.add("y", new JsonPrimitive(hexLoc.getY()));
		return jsonHexLoc;
	}
	
	/**
	 * serialize the ports on the map
	 * @return
	 */
	public JsonElement serializeMapPorts() {
		ArrayList<Port> mapPorts = map.getPortsOnMap();
		JsonArray jsonPorts = new JsonArray();
		for(Port port : mapPorts) {
			JsonElement jsonPort = serializeMapPort(port);
			jsonPorts.add(jsonPort);
		}
		return jsonPorts;
	}
	
	/**
	 * serialize each port
	 * @return
	 */
	public JsonElement serializeMapPort(Port port) {
		JsonObject jsonPort = new JsonObject();
		//resource, optional
		if(port.getType() != PortType.THREE) {
			jsonPort.add("resource", new JsonPrimitive(port.getType().name()));
		}
		//hex location
		JsonElement jsonHexLoc = serializeHexLocation(port.getEdgeLocation().getHexLoc());
		jsonPort.add("location", jsonHexLoc);
		//direction
		jsonPort.add("direction", new JsonPrimitive(port.getEdgeLocation().getDir().name()));
		//ratio
		jsonPort.add("ratio", new JsonPrimitive(port.getOfferRate()));
		return jsonPort;
	}
	
	/**
	 * serialize the roads on the map
	 * @return
	 */
	public JsonElement serializeMapRoads() {
		ArrayList<Road> roads = map.getRoadsOnMap();
		JsonArray jsonRoads = new JsonArray();
		for(Road road : roads) {
			JsonElement jsonRoad = serializeMapRoad(road);
			jsonRoads.add(jsonRoad);
		}
		return jsonRoads;
	}
	
	/**
	 * serialize an individual road
	 * @return
	 */
	public JsonElement serializeMapRoad(Road road) {
		JsonObject jsonRoad = new JsonObject();
		//the owner of road
		jsonRoad.add("owner", new JsonPrimitive(road.getOwner()));
		//the location of road
		JsonObject jsonLoc = new JsonObject();
		EdgeLocation roadLoc = road.getEdge().getLocation();
		jsonLoc.add("x", new JsonPrimitive(roadLoc.getHexLoc().getX()));
		jsonLoc.add("y", new JsonPrimitive(roadLoc.getHexLoc().getY()));
		jsonLoc.add("direction", new JsonPrimitive(roadLoc.getDir().name()));
		
		jsonRoad.add("location", jsonLoc);
		return jsonRoad;
	}
	
	/**
	 * serialize buildings, either city or settlement
	 * @param buildings
	 * @return
	 */
	public JsonElement serializeBuildings(ArrayList<Building> buildings) {
		JsonArray jsonBuildings = new JsonArray();
		for(Building building: buildings) {
			JsonElement jsonBuilding = serializeBuilding(building);
			jsonBuildings.add(jsonBuilding);
		}
		return jsonBuildings;
	}
	
	/**
	 * serialize an individual building
	 * @param building
	 * @return
	 */
	public JsonElement serializeBuilding(Building building) {
		JsonObject jsonBuilding = new JsonObject();
		//owner
		jsonBuilding.add("owner", new JsonPrimitive(building.getOwner()));
		//location
		JsonObject jsonVertex = new JsonObject();
		VertexLocation vertexLoc = building.getVertex().getLocation();
		jsonVertex.add("x", new JsonPrimitive(vertexLoc.getHexLoc().getX()));
		jsonVertex.add("y", new JsonPrimitive(vertexLoc.getHexLoc().getY()));
		jsonVertex.add("direction", new JsonPrimitive(vertexLoc.getDir().name()));
		
		jsonBuilding.add("location", jsonVertex);
		
		return jsonBuilding;
	}
	
	/**
	 * serialize the players 
	 * @return json array of players
	 */
	public JsonElement serializePlayers() {
		List<User> users = turnManager.getUsers();
		JsonArray jsonUsers = new JsonArray();
		for(User user : users) {
			JsonElement jsonUser = serializePlayer(user);
			jsonUsers.add(jsonUser);
		}
		return jsonUsers;
	}
	
	/**
	 * serialize a single player
	 * @return json player
	 */
	public JsonElement serializePlayer(User user) {
		JsonObject jsonUser = new JsonObject();
		jsonUser.add("cities", new JsonPrimitive(user.getUnusedCities()));
		jsonUser.add("color", new JsonPrimitive(user.getCatanColor().name()));
		jsonUser.add("discarded", new JsonPrimitive(user.getHasDiscarded()));
		jsonUser.add("monuments", new JsonPrimitive(user.getMonumentsPlayed()));
		jsonUser.add("name", new JsonPrimitive(user.getName()));
		jsonUser.add("newDevCards", serializeDevCards(user.getNewDevCardDeck()));
		jsonUser.add("oldDevCards", serializeDevCards(user.getUsableDevCardDeck()));
		jsonUser.add("playerIndex", new JsonPrimitive(user.getTurnIndex()));
		jsonUser.add("playedDevCard", new JsonPrimitive(user.getHasPlayedDevCard()));
		jsonUser.add("playerID", new JsonPrimitive(user.getPlayerID()));
		jsonUser.add("resources", serializeResourceList(user.getResourceCards()));
		jsonUser.add("roads", new JsonPrimitive(user.getUnusedRoads()));
		jsonUser.add("settlements", new JsonPrimitive(user.getUnusedSettlements()));
		jsonUser.add("soldiers", new JsonPrimitive(user.getSoldiers()));
		jsonUser.add("victoryPoints", new JsonPrimitive(user.getVictoryPoints()));
		return jsonUser;
	}
	
	/**
	 * help serialize the dev card decks
	 * @param devCards
	 * @return
	 */
	public JsonElement serializeDevCards(DevCardDeck devCards) {
		JsonObject jsonDevCards = new JsonObject();
		jsonDevCards.add("monopoly", new JsonPrimitive(devCards.getCountByType(DevCardType.MONOPOLY)));
		jsonDevCards.add("monument", new JsonPrimitive(devCards.getCountByType(DevCardType.MONUMENT)));
		jsonDevCards.add("roadBuilding", new JsonPrimitive(devCards.getCountByType(DevCardType.ROAD_BUILD)));
		jsonDevCards.add("soldier", new JsonPrimitive(devCards.getCountByType(DevCardType.SOLDIER)));
		jsonDevCards.add("yearOfPlenty", new JsonPrimitive(devCards.getCountByType(DevCardType.YEAR_OF_PLENTY)));
		return jsonDevCards;
	}
	
	/**
	 * serialize the trade offer
	 * @return json of trade offer
	 */
	public JsonElement serializeTradeOffer() {
		JsonObject jsonTrade = new JsonObject();
		jsonTrade.add("sender", new JsonPrimitive(tradeOffer.getSenderIndex()));
		jsonTrade.add("receiver", new JsonPrimitive(tradeOffer.getReceiverIndex()));
		//offer resource list, pos = offered, neg = asked for
		jsonTrade.add("offer", serializeOfferList());
		return jsonTrade;
	}
	
	public JsonElement serializeOfferList() {
		int brick = getResourceCount(ResourceType.BRICK, tradeOffer.getSendingDeck(), tradeOffer.getReceivingDeck());
		int ore = getResourceCount(ResourceType.ORE, tradeOffer.getSendingDeck(), tradeOffer.getReceivingDeck());
		int sheep = getResourceCount(ResourceType.SHEEP, tradeOffer.getSendingDeck(), tradeOffer.getReceivingDeck());
		int wheat = getResourceCount(ResourceType.WHEAT, tradeOffer.getSendingDeck(), tradeOffer.getReceivingDeck());
		int wood = getResourceCount(ResourceType.WOOD, tradeOffer.getSendingDeck(), tradeOffer.getReceivingDeck());
		
		JsonObject jsonOffer = new JsonObject();
		
		jsonOffer.add("brick", new JsonPrimitive(brick));
		jsonOffer.add("ore", new JsonPrimitive(ore));
		jsonOffer.add("sheep", new JsonPrimitive(sheep));
		jsonOffer.add("wheat", new JsonPrimitive(wheat));
		jsonOffer.add("wood", new JsonPrimitive(wood));
		
		return jsonOffer;
	}
	
	public int getResourceCount(ResourceType resource, ResourceCardDeck sendDeck, ResourceCardDeck recDeck) {
		int sendCount = sendDeck.getCountByType(resource);
		int recCount = recDeck.getCountByType(resource);
		if(sendCount >= recCount) {
			return sendCount;
		}
		else{
			return recCount * -1;
		}
	}
	
	public JsonElement serializeTurnTracker() {
		JsonObject jsonTurn = new JsonObject();
		jsonTurn.add("currentTurn", new JsonPrimitive(turnManager.getCurrentTurn()));
		jsonTurn.add("status", new JsonPrimitive(turnManager.currentTurnPhase().name()));
		jsonTurn.add("longestRoad", new JsonPrimitive(scoreKeeper.getLongestRoadUser()));
		jsonTurn.add("largestArmy", new JsonPrimitive(scoreKeeper.getLargestArmyUser()));
		return jsonTurn;
	}
	
	
	/**
	 * deserialize the model from the JSON response
	 * @param jsonModel
	 */
	
	public void deserialize(JsonElement jsonModel) {
		//Avoid Multiple updates at the same time
		if (isUpdating) {
			return;
		}
		
		isUpdating = true;
		
		//would model be better as jsonElement?
		if(jsonModel.isJsonObject()) {
			
			map = new Map(new ArrayList<HexTile>());
			JsonObject jsonObject = jsonModel.getAsJsonObject();
			
			//get bank resource cards
			ArrayList<ResourceCard> bankResourceCards = extractResourceDeck(jsonObject);
			//get bank dev cards
			ArrayList<DevCard> bankDevCards = extractDevDeck(jsonObject);
			
			//update bank
			bank.setDevCardDeck(new DevCardDeck(bankDevCards));
			bank.setResourceCardDeck(new ResourceCardDeck(bankResourceCards));
			
			//chat
			JsonObject jsonChat = jsonModel.getAsJsonObject().get("chat").getAsJsonObject(); 
			chat = extractMessageList(jsonChat);
			
			//log
			JsonObject jsonLog = jsonModel.getAsJsonObject().get("log").getAsJsonObject();
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
			
			else {
				tradeOffer = null;
			}

			//turn manager
			JsonObject jsonTurnManager = jsonModel.getAsJsonObject().get("turnTracker").getAsJsonObject();
			updateTurnManager(jsonTurnManager);
			
			//version
			version = jsonModel.getAsJsonObject().get("version").getAsInt();
			
			//winner
			winner = jsonModel.getAsJsonObject().get("winner").getAsInt();
			scoreKeeper.setWinner(winner);
			updateScoreKeeper();
			
		}
		
		isUpdating = false;
	}
	
	/**
	 * get the resource(bank) deck from the model
	 * @param jsonModel
	 * @return
	 */
	
	public ArrayList<ResourceCard> extractResourceDeck(JsonObject jsonModel) {
		ArrayList<ResourceCard> bankResourceCards = new ArrayList<ResourceCard>();
		
		JsonObject jsonDeck = jsonModel.get("bank").getAsJsonObject();
		
		populateResourceDeck(jsonDeck, bankResourceCards, null);
		
		return bankResourceCards;
	}
	
	/**
	 * get the available developer cards from model
	 * @param jsonModel
	 * @return
	 */
	public ArrayList<DevCard> extractDevDeck(JsonObject jsonModel) {
		
		ArrayList<DevCard> bankDevCards = new ArrayList<DevCard>();
		
		JsonObject jsonDeck = jsonModel.get("deck").getAsJsonObject();
		
		populateDevCardDeck(jsonDeck, bankDevCards);
		
		return bankDevCards;
	}
	
	/**
	 * helper function to populate a dev card deck
	 * @param jsonDeck
	 * @param devCards
	 */
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
	
	/**
	 * helper function to add dev cards a certain number of times 
	 * @param deckToAdd
	 * @param numTimes
	 * @param cardType
	 */
	public void addDevCardsByNum(ArrayList<DevCard> deckToAdd, int numTimes, DevCardType cardType) {
			for(int i = 0; i < numTimes; i++) {
				deckToAdd.add(new DevCard(cardType));
			}
	}
	
	/**
	 * extracts the list of messages for chat or game log
	 * @param jsonMessageList
	 * @return
	 */
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
	
	/**
	 * gets the list of map hex tiles from the json map
	 * @param jsonMap
	 * @return
	 */
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
	
	/**
	 * populate each individual hex tile with correct info
	 * @param jsonHexTile
	 * @return
	 */
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
	
	/**
	 * get the ports from json map
	 * @param jsonMap
	 * @return
	 */
	public ArrayList<Port> extractPorts(JsonObject jsonMap) {
		ArrayList<Port> portsOnMap = new ArrayList<Port>();
		
		JsonArray jsonPorts = jsonMap.get("ports").getAsJsonArray();
		
		for(JsonElement jsonElePort : jsonPorts) {
			Port port = extractPort(jsonElePort.getAsJsonObject());
			portsOnMap.add(port);
		}
		
		return portsOnMap;
	}
	
	/**
	 * get the info of each port 
	 * @param jsonPort
	 * @return
	 */
	public Port extractPort(JsonObject jsonPort) {
		
		Gson gson = new Gson();
		PortType portType;
		
		if(jsonPort.get("resource") != null) {
			portType = gson.fromJson(jsonPort.get("resource"), PortType.class);
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
		port.setEdgeLocation(location);
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
	
	public void extractUsers(JsonObject jsonModel){
		
		JsonArray jsonUserArray = jsonModel.get("players").getAsJsonArray();
		
		for(int i = 0; i < jsonUserArray.size(); i++) {
			if(jsonUserArray.get(i).isJsonNull()) {
				continue;
			}
			JsonObject jsonUser = jsonUserArray.get(i).getAsJsonObject();

			updateUser(jsonUser);
		}
	}
	
	
	public void updateUser(JsonObject jsonUser) {
		Gson gson = new Gson();
		
		int numCities = jsonUser.get("cities").getAsInt();
		CatanColor userColor = gson.fromJson(jsonUser.get("color"), CatanColor.class);
		//discarded -- whether or not user has already discarded cards this turn
		boolean hasDiscarded = jsonUser.get("discarded").getAsBoolean();
		
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

		currUser.setMonumentsPlayed(monumentsPlayed); 
		currUser.getHand().setNewDevCardDeck(newDevCardDeck);
		currUser.getHand().setDevCardDeck(oldDevCardDeck);
		currUser.getHand().setResourceCardDeck(resourceDeck);
		
		currUser.setHasDiscarded(hasDiscarded);
		
		populateUserResourceInfo(jsonUser.get("resources").getAsJsonObject(), currUser);
		
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
		user.resetOccupiedEdges();
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
	
	public void populateUserResourceInfo(JsonObject jsonDeck, User user) {
		int brickCount = jsonDeck.get("brick").getAsInt();
		int oreCount = jsonDeck.get("ore").getAsInt();
		int sheepCount = jsonDeck.get("sheep").getAsInt();
		int wheatCount = jsonDeck.get("wheat").getAsInt();
		int woodCount = jsonDeck.get("wood").getAsInt();
		
		user.setBrickCards(brickCount);
		user.setOreCards(oreCount);
		user.setSheepCards(sheepCount);
		user.setWheatCards(wheatCount);
		user.setWoodCards(woodCount);
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
		
		String turn = gson.fromJson(jsonTurnManager.get("status"), String.class);
		TurnPhase currTurnPhase = gson.fromJson(jsonTurnManager.get("status"), TurnPhase.class);

		//index of user with longest road, -1 if none
		int longestRoad = jsonTurnManager.get("longestRoad").getAsInt();
		
		int largestArmy = jsonTurnManager.get("largestArmy").getAsInt();
		
		turnManager.setCurrentTurn(currentTurn);
		turnManager.setCurrentPhase(currTurnPhase);
		turnManager.setLongestRoadIndex(longestRoad);
		turnManager.setLargestArmyIndex(largestArmy);
		
		//update in score keeper
		scoreKeeper.setLongestRoadUser(longestRoad);
		scoreKeeper.setLargestArmyUser(largestArmy);
		
	}
	
	public void updateScoreKeeper() {
		ArrayList<User> users = new ArrayList<User>( turnManager.getUsers());
		
		for(User user : users) {
			scoreKeeper.setScore(user.getTurnIndex(), user.getVictoryPoints());
		}
	}

}
