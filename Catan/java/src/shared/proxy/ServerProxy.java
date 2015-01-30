package shared.proxy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

import shared.model.Model;
import shared.proxy.game.*;
import shared.proxy.games.*;
import shared.proxy.moves.*;
import shared.proxy.user.*;
import shared.proxy.util.*;

/**
 * Proxy used by the client to send commands to the server
 * @author Kent
 *
 */
public class ServerProxy implements Proxy{

	private static String SERVER_HOST = "localhost";
	private static int SERVER_PORT = 8081;
	private static String URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	private static final String HTTP_GET = "GET";
	private static final String HTTP_POST = "POST";
	
	private XStream jsonStream;
	/** Default Constructor*/
	public ServerProxy(){
		jsonStream = new XStream(new JsonHierarchicalStreamDriver());
	}
	
	public ServerProxy(String host, String port){
		jsonStream = new XStream(new JsonHierarchicalStreamDriver());
		SERVER_HOST = host;
		SERVER_PORT = Integer.parseInt(port);
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	private Object doGet(String urlPath) throws ProxyException{
		try{
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_GET);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				Object result = jsonStream.fromXML(connection.getInputStream());
				return result;
			}
			else{
				throw new ProxyException(String.format("doGet failed: %s (http code %d)",
						urlPath, connection.getResponseCode()));
			}	
		}
		catch (IOException e) {
			throw new ProxyException(String.format("doGet failed: %s", e.getMessage()), e);
		}
	}
	
	private Object doPost(String urlPath, Object postData) throws ProxyException{
		try{
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.addRequestProperty("Accept", "text/html");
			connection.connect();
			jsonStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				Object result = jsonStream.fromXML(connection.getInputStream());
				return result;
			}
			else{
				throw new ProxyException(String.format("doPost failed: %s (http code %d)",
						urlPath, connection.getResponseCode()));
			}
		}
		catch (IOException e) {
			throw new ProxyException(String.format("doPost failed: %s", e.getMessage()), e);
		}
	}
	@Override
	public void login(Credentials cred) throws ProxyException {
		doPost("/user/login", cred);
		
	}

	@Override
	public void register(Credentials cred) throws ProxyException {
		doPost("/user/register", cred);
		
	}

	@Override
	public List<Game> list() throws ProxyException {
		return (List<Game>)doGet("/games/list");
	}

	@Override
	public NewGame create(CreateGameRequest CreateRequest) throws ProxyException {
		return (NewGame)doPost("/games/create", CreateRequest);
	}

	@Override
	public void join(JoinGameRequest JoinRequest) throws ProxyException {
		doPost("/games/join", JoinRequest);
		
	}

	@Override
	public void save(SaveGameRequest SaveRequest) throws ProxyException {
		doPost("/games/save", SaveRequest);
	}

	@Override
	public void load(LoadGameRequest LoadRequest) throws ProxyException {
		doPost("/games/load", LoadRequest);
		
	}

	@Override
	public Model model(int version) throws ProxyException {
		return (Model)doGet("/game/model");
	}

	@Override
	public Model reset() throws ProxyException {
		return (Model)doGet("/game/reset");
	}

	@Override
	public Model postCommands(List<String> commandslist) throws ProxyException {
		return (Model)doPost("/game/commands", commandslist);
	}

	@Override
	public List<String> getCommands() throws ProxyException {
		return (List<String>)doGet("/game/commands");
	}

	@Override
	public Model sendChat(SendChat sendchat) throws ProxyException {
		return (Model)doPost("/moves/sendChat", sendchat);
	}

	@Override
	public Model rollNumber(RollNumber rollnum) throws ProxyException {
		return (Model)doPost("/moves/rollNumber", rollnum);
	}

	@Override
	public Model robPlayer(RobPlayer robplayer) throws ProxyException {
		return (Model)doPost("/moves/robPlayer", robplayer);
	}

	@Override
	public Model finishTurn(FinishMove finishmove) throws ProxyException {
		return (Model)doPost("/moves/finishTurn", finishmove);
	}

	@Override
	public Model buyDevCard(BuyDevCard buydev) throws ProxyException {
		return (Model)doPost("/moves/buyDevCard", buydev);
	}

	@Override
	public Model Year_of_Plenty(Year_of_Plenty_ yop) throws ProxyException {
		return (Model)doPost("/moves/Year_of_Plenty", yop);
	}

	@Override
	public Model Road_Building(Road_Building_ roadbuild) throws ProxyException {
		return (Model)doPost("/moves/Road_Building", roadbuild);
	}

	@Override
	public Model Soldier(Soldier_ soldier) throws ProxyException {
		return (Model)doPost("/moves/Soldier", soldier);
	}

	@Override
	public Model Monopoly(Monopoly_ monopoly) throws ProxyException {
		return (Model)doPost("/moves/Monopoly", monopoly);
	}

	@Override
	public Model Monument(Monument_ monument) throws ProxyException {
		return (Model)doPost("/moves/Monument", monument);
	}

	@Override
	public Model buildRoad(BuildRoad buildroad) throws ProxyException {
		return (Model)doPost("/moves/buildRoad", buildroad);
	}

	@Override
	public Model buildSettlement(BuildSettlement buildsettlement) throws ProxyException {
		return (Model)doPost("/moves/buildSettlement", buildsettlement);
	}

	@Override
	public Model buildCity(BuildCity buildcity) throws ProxyException {
		return (Model)doPost("/moves/buildCity", buildcity);
	}

	@Override
	public Model offerTrade(OfferTrade tradeOffer) throws ProxyException {
		return (Model)doPost("/moves/offerTrade", tradeOffer);
	}

	@Override
	public Model acceptTrade(AcceptTrade tradeAccept) throws ProxyException {
		return (Model)doPost("/moves/acceptTrade", tradeAccept);
	}

	@Override
	public Model maritimeTrade(MaritimeTrade tradeMaritime) throws ProxyException {
		return (Model)doPost("/moves/maritimeTrade", tradeMaritime);
	}

	@Override
	public Model discardCards(DiscardCards discard) throws ProxyException {
		return (Model)doPost("/moves/discardCards", discard);
	}

	@Override
	public void changeLogLevel(ChangeLogLevelRequest cllr) throws ProxyException {
		doPost("util/changeLogLevel", cllr);
		
	}

	@Override
	public void addAI(AddAIRequest addAIreq) throws ProxyException {
		doPost("/game/addAI", addAIreq);
		
	}

	@Override
	public List<String> listAI() throws ProxyException {
		return (List<String>)doGet("/game/listAI");
	}

}
