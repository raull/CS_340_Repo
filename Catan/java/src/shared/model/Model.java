package shared.model;

import shared.model.board.Map;
import shared.model.cards.ResourceCardDeck;
import shared.model.game.MessageList;
import shared.model.game.TradeOffer;
import shared.model.game.TurnManager;
import shared.model.game.UserManager;

import com.google.gson.JsonObject;

public class Model {
	
	//according to the client model json spec
	public ResourceCardDeck bank; //cards available to be distributed to the players
	public MessageList chat; //all chat messages
	public MessageList log; //all log messages
	public Map map;
	public UserManager users; //list of users
	public TradeOffer tradeOffer; //current trade offer, if there is one
	public TurnManager turnManager; //the turntracker -- tracks who's turn it is
	public int version; //version of the model, to see if up to date
	public int winner; //-1 when nobody won yet. when someone wins, it's their order index
	
	
	
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
		
	}
}
