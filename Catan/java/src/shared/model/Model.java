package shared.model;

import java.util.ArrayList;
import java.util.Collection;

import shared.definitions.DevCardType;
import shared.definitions.HexType;
import shared.locations.HexLocation;
import shared.model.board.HexTile;
import shared.model.board.Map;
import shared.model.cards.DevCard;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.MessageList;
import shared.model.game.TradeOffer;
import shared.model.game.TurnManager;
import shared.model.game.UserManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

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
		//would model be better as jsonelement?
		if(jsonModel.isJsonObject()) {
			
			
			JsonObject jsonObject = jsonModel.getAsJsonObject();
			
			//get deck
			ArrayList<DevCard> bankDevCards = extractDeck(jsonObject);
			
			//get map
			
				//get roads
				
				//get cities
				
				//get settlements
				
				//radius??
				
				//get ports
				
				//get robber
			
			//players
				//resources
				//old dev cards
				//new dev cards
				//roads
				//cities
				//settlements
				//soldiers
				//victory points
				//monuments
				//played dev card
				//discarded
				//player id
				//player index
				//name
				//color
			
			//log
			//chat
			//bank
			//turn manager
			//winner
			//version
			
			JsonArray tempArray = jsonObject.get("hexes").getAsJsonArray();
			
		}
	}
	
	public ArrayList<DevCard> extractDeck(JsonObject jsonModel) {
		
		ArrayList<DevCard> bankDevCards = new ArrayList<DevCard>();
		
		JsonObject jsonDeck = jsonModel.get("deck").getAsJsonObject();
		
		int yopCount = jsonDeck.get("yearOfPlenty").getAsInt();
		int monopolyCount = jsonDeck.get("monopoly").getAsInt();
		int soldierCount = jsonDeck.get("soldier").getAsInt();
		int roadBuildingCount = jsonDeck.get("roadBuilding").getAsInt();
		int monumentCount = jsonDeck.get("monument").getAsInt();
		
		addDevCardsByNum(bankDevCards, yopCount, DevCardType.YEAR_OF_PLENTY);
		addDevCardsByNum(bankDevCards, monopolyCount, DevCardType.MONOPOLY);
		addDevCardsByNum(bankDevCards, soldierCount, DevCardType.SOLDIER);
		addDevCardsByNum(bankDevCards, roadBuildingCount, DevCardType.ROAD_BUILD);
		addDevCardsByNum(bankDevCards, monumentCount, DevCardType.MONUMENT);
		
		return bankDevCards;
	}
	
	public void addDevCardsByNum(ArrayList<DevCard> deckToAdd, int numTimes, DevCardType cardType) {
			for(int i = 0; i < numTimes; i++) {
				deckToAdd.add(new DevCard(cardType));
			}
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
	
	
}
