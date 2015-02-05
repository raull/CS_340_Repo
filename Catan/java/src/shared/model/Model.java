package shared.model;

import java.util.ArrayList;
import java.util.Collection;

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
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.board.Port;
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
import shared.model.game.TradeOffer;
import shared.model.game.TurnManager;
import shared.model.game.TurnPhase;
import shared.model.game.User;
import shared.model.game.UserManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Model {
	
	//according to the client model json spec
	public ResourceCardDeck bank; //cards available to be distributed to the players
	public MessageList chat; //all chat messages
	public MessageList log; //all log messages
	public Map map; //game map
	public UserManager users; //list of users
	public TradeOffer tradeOffer; //current trade offer, if there is one
	public TurnManager turnManager; //the turntracker -- tracks who's turn it is
	public int version; //version of the model, to see if up to date
	public int winner; //-1 when nobody won yet. when someone wins, it's their order index
	
	public Model(){}
	
	public Model(ResourceCardDeck bank, MessageList chat, MessageList log,
			Map map, UserManager users, TradeOffer tradeOffer,
			TurnManager turnManager, int version, int winner) {
		super();
		this.bank = bank;
		this.chat = chat;
		this.log = log;
		this.map = map;
		this.users = users;
		this.tradeOffer = tradeOffer;
		this.turnManager = turnManager;
		this.version = version;
		this.winner = winner;
	}

	public ResourceCardDeck getBank() {
		return bank;
	}

	public void setBank(ResourceCardDeck bank) {
		this.bank = bank;
	}

	public MessageList getChat() {
		return chat;
	}

	public void setChat(MessageList chat) {
		this.chat = chat;
	}

	public MessageList getLog() {
		return log;
	}

	public void setLog(MessageList log) {
		this.log = log;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public UserManager getUsers() {
		return users;
	}

	public void setUsers(UserManager users) {
		this.users = users;
	}

	public TradeOffer getTradeOffer() {
		return tradeOffer;
	}

	public void setTradeOffer(TradeOffer tradeOffer) {
		this.tradeOffer = tradeOffer;
	}

	public TurnManager getTurnManager() {
		return turnManager;
	}

	public void setTurnManager(TurnManager turnManager) {
		this.turnManager = turnManager;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public int getWinner() {
		return winner;
	}

	public void setWinner(int winner) {
		this.winner = winner;
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
			
			//create bank for model
			Bank bank = new Bank(bankDevCards, bankResourceCards);
			
			//chat
			JsonObject jsonChat = jsonModel.get("chat").getAsJsonObject(); 
			MessageList chat = extractMessageList(jsonChat);
			
			//log
			JsonObject jsonLog = jsonModel.get("log").getAsJsonObject();
			MessageList log = extractMessageList(jsonLog);
			
			//get map
				//get hexes
			ArrayList<HexTile> mapHexTiles = extractHexes(jsonObject);
			
				//get roads
				
				//get cities
				
				//get settlements
				
				//radius??
				
				//get ports
				
				//get robber
			
			//players
			
			//trade offer -- may be null
			if(jsonObject.get("tradeOffer") != null) {
				JsonObject tradeOffer = jsonObject.get("tradeOffer").getAsJsonObject();
				
			}

			//turn manager
			JsonObject jsonTurnManager = jsonModel.get("turnTracker").getAsJsonObject();
			
			//version
			int version = jsonModel.get("version").getAsInt();
			
			//winner
			int winner = jsonModel.get("winner").getAsInt();
			
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
	
	public ArrayList<HexTile> extractHexes(JsonObject jsonModel) {
		ArrayList<HexTile> mapHexTiles = new ArrayList<HexTile>();
		
		JsonArray jsonHexArray = jsonModel.get("hexes").getAsJsonArray();
		
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
	
		HexType hexType = gson.fromJson(jsonHexTile.get("resource"), HexType.class);
		HexLocation hexLocation = gson.fromJson(jsonHexTile.get("location"), HexLocation.class);
		int number = jsonHexTile.get("number").getAsInt();
		
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
		
		VertexDirection portDirection = gson.fromJson(jsonPort.get("direction"), VertexDirection.class);
		
		int ratio = jsonPort.get("ratio").getAsInt();
		
		Port port = new Port(portType, ratio);
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
		return road;
	}
	
	public ArrayList<Building> extractBuildings(JsonObject jsonMap, String buildingType) {
		ArrayList<Building> buildings = new ArrayList<Building>();
		
		//building type is settlements or cities
		JsonArray jsonBuildings = jsonMap.get(buildingType).getAsJsonArray();
		
		for(JsonElement jsonEleBuilding : jsonBuildings) {
			Building building = extractBuilding(jsonEleBuilding.getAsJsonObject());
			buildings.add(building);
		}
		
		return buildings;
	}
	
	public Building extractBuilding(JsonObject jsonBuilding) {
		Gson gson = new Gson();
		
		int owner = jsonBuilding.get("owner").getAsInt();
		
		JsonObject jsonLocation = jsonBuilding.get("location").getAsJsonObject();
		int x = jsonLocation.get("x").getAsInt();
		int y = jsonLocation.get("y").getAsInt();
		HexLocation hexLocation = new HexLocation(x, y);
		VertexDirection direction = gson.fromJson(jsonLocation.get("direction"), VertexDirection.class);
		
		VertexLocation location = new VertexLocation(hexLocation, direction);
		
		Building building = new Building();
		return building;
	}
	
	public void extractRobber(JsonObject jsonMap, Map map) {
		
		JsonObject jsonLocation = jsonMap.get("robber").getAsJsonObject();
		int x = jsonLocation.get("x").getAsInt();
		int y = jsonLocation.get("y").getAsInt();
		HexLocation hexLocation = new HexLocation(x, y);
		
		HexTile robberTile = map.getHexTileByLocation(hexLocation);
		robberTile.setRobber(true);
	}
	
	public ArrayList<User> extractUsers(JsonObject jsonModel) {
		ArrayList<User> currentPlayers = new ArrayList<User>(); //prob updating the users in turnmanager
		
		JsonArray jsonUserArray = jsonModel.get("players").getAsJsonArray();
		
		for(JsonElement jsonEleUser : jsonUserArray) {
			JsonObject jsonUser = jsonEleUser.getAsJsonObject();
			User user = extractUser(jsonUser);
		}
		return currentPlayers;
	}
	
	public void addPieceToList(ArrayList<Piece> userPieces, PieceType pieceType) {
		
	}
	
	public User extractUser(JsonObject jsonUser) {
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
		
		int solders = jsonUser.get("soldiers").getAsInt();
		
		int victoryPoints = jsonUser.get("victoryPoints").getAsInt();
		
		User user = new User(name, name, userColor); //need name, pw, color..
		return user;
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
			addResourceCardsByNum(resourceCards2, count, resourceType);
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
	
	public TradeOffer tradeOffer(JsonObject jsonTradeOffer) {
		
		int senderIndex = jsonTradeOffer.get("sender").getAsInt();
		int receiverIndex = jsonTradeOffer.get("receiver").getAsInt();
		
		//positives: resources being offered
		//negatives: resources being asked for
		
		ArrayList<ResourceCard> offeredCards = new ArrayList<ResourceCard>();
		ArrayList<ResourceCard> requestedCards = new ArrayList<ResourceCard>();
		
		populateResourceDeck(jsonTradeOffer.get("offer").getAsJsonObject(), offeredCards, requestedCards);
		
		TradeOffer tradeOffer = new TradeOffer(0, new ResourceCardDeck(offeredCards), new ResourceCardDeck(requestedCards));
		
		return tradeOffer;
	}
	
	public TurnManager extractTurnTracker(JsonObject jsonModel) {
		Gson gson = new Gson();
		
		TurnManager turnManager = new TurnManager(null); //add array list of users
		
		JsonObject jsonTurnManager = jsonModel.get("turnTracker").getAsJsonObject();
		
		int currentTurn = jsonTurnManager.get("currentTurn").getAsInt();
		
		TurnPhase currTurnPhase = gson.fromJson(jsonTurnManager.get("status"), TurnPhase.class);
		
		//index of user with longest road, -1 if none
		int longestRoad = jsonTurnManager.get("longestRoad").getAsInt();
		
		int largestArmy = jsonTurnManager.get("largestArmy").getAsInt();
		
		return turnManager;
	}
	
}
