package shared.proxy;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import shared.model.Model;
import shared.model.cards.*;
import shared.model.game.*;
import shared.proxy.game.*;
import shared.proxy.games.*;
import shared.proxy.user.*;
import shared.proxy.util.*;
import shared.proxy.moves.*;
/**
 * A Mock Proxy, or "Moxy", used primarily for unit testing when 
 * not connected to a server. Returns hardcoded results for the typical 
 * Proxy functions rather than connecting to a server.
 * @author Kent
 *
 */
public class Moxy implements Proxy{

	private Model model;
	private JsonObject JSONModel;
	
	
	public Moxy() {
		/*JSONModel = new JsonObject();
		JsonReader jsonReader = Json.createReader(new StringReader("[{\"title\": \"Default Game\","
    +"\"id\": 0, \"players\": [ {\"color\": \"orange\", \"name\": \"Sam\", \"id\": 0},"
    +"  { \"color\": \"blue\",\"name\": \"Brooke\",\"id\": 1},{ \"color\": \"red\", \"name\": \"Pete\","
    +" \"id\": 10},{\"color\": \"green\",\"name\": \"Mark\",\"id\": 11}]},{\"title\": \"AI Game\","
    +" \"id\": 1,\"players\": [{\"color\": \"orange\", \"name\": \"Pete\",\"id\": 10},"
    +"  {\"color\": \"puce\",\"name\": \"Steve\",\"id\": -2 },{\"color\": \"red\",\"name\": \"Ken\","
    +" \"id\": -3}, { \"color\": \"blue\", \"name\": \"Miguel\",\"id\": -4}]},{\"title\": \"Empty Game\",
    "id": 2,
    "players": [
      {
        "color": "orange",
        "name": "Sam",
        "id": 0
      },
      {
        "color": "blue",
        "name": "Brooke",
        "id": 1
      },
      {
        "color": "red",
        "name": "Pete",
        "id": 10
      },
      {
        "color": "green",
        "name": "Mark",
        "id": 11
      }
    ]
  },
  {
    "title": "Sweet Game",
    "id": 3,
    "players": [
      {
        "color": "red",
        "name": "tiger",
        "id": 12
      },
      {
        "color": "yellow",
        "name": "Sam",
        "id": 0
      },
      {},
      {}
    ]
  }
]'
		/*
		Bank bank = new Bank();
		MessageList msglist = new MessageList();
		MessageList log = new MessageList();
		
		model = new */
		
		
		this.model = getModel("model.json");
	}
	
	public Model getModel(String filepath){
		JsonObject json = new JsonObject();
		JsonParser parser = new JsonParser();
		JsonElement jsonElement;
		
		Model model = new Model();
		
		try {
			jsonElement = parser.parse(new FileReader(filepath));
			Gson gson = new GsonBuilder().create();
			model = gson.fromJson(jsonElement, Model.class);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}
	
	public Moxy(Model model) {
		super();
		this.model = model;
	}

	@Override
	public void login(Credentials cred) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void register(Credentials cred) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Game> list() {
		/*Player jake = new Player("Jake", "red", 0);
		Player sara = new Player("Sara", "blue", 1);
		Player taylor = new Player("Taylor", "green", 2);
		Player[] players = new Player[3];
		players[0] = jake;
		players[1] = sara;
		players[2] = taylor;
		Game game1 = new Game("Easy Game", 1, players);
		Game game2 = new Game("Hard Game", 2, players);
		List<Game> gamelist = new ArrayList<Game>();
		gamelist.add(game1);
		gamelist.add(game2);*/
		
		JsonObject json = new JsonObject();
		
		JsonParser parser = new JsonParser();
		JsonElement jsonElement;
		List<Game> gamelist = new ArrayList<Game>();
		try {
			jsonElement = parser.parse(new FileReader("gamelist.json"));
			Gson gson = new GsonBuilder().create();
			gamelist = gson.fromJson(jsonElement, new TypeToken<List<Game>>(){}.getType());
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gamelist;
	}

	@Override
	public NewGame create(CreateGameRequest CreateRequest) {
		EmptyPlayer[] players = new EmptyPlayer[3];
		NewGame nugame = new NewGame(CreateRequest.getName(), 3, players);
		return nugame;
	}

	@Override
	public void join(JoinGameRequest JoinRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(SaveGameRequest SaveRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void load(LoadGameRequest LoadRequest) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Model model(int version) {
		return model;
	}

	@Override
	public Model reset() {
		return model;
	}

	@Override
	public Model postCommands(List<String> commandslist) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getCommands() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model sendChat(SendChat sendchat) {
		return getModel("chatmodel.json");
	}

	@Override
	public Model rollNumber(RollNumber rollnum) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model robPlayer(RobPlayer robplayer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model finishTurn(FinishMove finishmove) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model buyDevCard(BuyDevCard buydev) {
		return getModel("buydev.json");
	}

	@Override
	public Model Year_of_Plenty(Year_of_Plenty_ yop) {
		return getModel("yop.json");
	}

	@Override
	public Model Road_Building(Road_Building_ roadbuild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Soldier(Soldier_ soldier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Monopoly(Monopoly_ monopoly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model Monument(Monument_ monument) {
		return getModel("monument.json");
	}

	@Override
	public Model buildRoad(BuildRoad buildroad) {
		return getModel("bldrd.json");
	}

	@Override
	public Model buildSettlement(BuildSettlement buildsettlement) {
		return getModel("bldsetl.json");
	}

	@Override
	public Model buildCity(BuildCity buildcity) {
		return getModel("bldcty.json");
	}

	@Override
	public Model offerTrade(OfferTrade tradeOffer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model acceptTrade(AcceptTrade tradeAccept) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model maritimeTrade(MaritimeTrade tradeMaritime) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model discardCards(DiscardCards discard) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeLogLevel(ChangeLogLevelRequest cllr) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAI(AddAIRequest addAIreq) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> listAI() {
		// TODO Auto-generated method stub
		return null;
	}

}
