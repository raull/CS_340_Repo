package shared.proxy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.JsonObject;
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
	private String usercookie;
	private String gameID;
	
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
			connection.setRequestProperty("Cookie", "catan.user="+ usercookie + "; catan.game=" + gameID);
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
	
	private Object doLogin(String urlPath, Object postData) throws ProxyException{
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
				String cookieheader = connection.getHeaderField("Set-cookie");
				StringBuilder sb = new StringBuilder(cookieheader);
				sb.delete(0, 10);
				int length = sb.length();
				sb.delete(length-8, length-1);
				setUsercookie(sb.toString());
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
	
	private Object doJoin(String urlPath, Object postData) throws ProxyException{
		try{
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.addRequestProperty("Accept", "text/html");
			connection.setRequestProperty("Cookie", usercookie);
			connection.connect();
			jsonStream.toXML(postData, connection.getOutputStream());
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				String cookieheader = connection.getHeaderField("Set-cookie");
				StringBuilder sb = new StringBuilder(cookieheader);
				sb.substring(11, 12);
				setGameID(sb.toString());
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
	
	public String getUsercookie() {
		return usercookie;
	}

	public void setUsercookie(String usercookie) {
		this.usercookie = usercookie;
	}

	public String getGameID() {
		return gameID;
	}

	public void setGameID(String gameID) {
		this.gameID = gameID;
	}

	@Override
	public void login(Credentials cred) throws ProxyException {
		doLogin("/user/login", cred);
		
	}

	@Override
	public void register(Credentials cred) throws ProxyException {
		doLogin("/user/register", cred);
		
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
		doJoin("/games/join", JoinRequest);
		
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
	public JsonObject model(int version) throws ProxyException {
		return (JsonObject)doGet("/game/model");
	}

	@Override
	public JsonObject reset() throws ProxyException {
		return (JsonObject)doGet("/game/reset");
	}

	@Override
	public JsonObject postCommands(List<String> commandslist) throws ProxyException {
		return (JsonObject)doPost("/game/commands", commandslist);
	}

	@Override
	public JsonObject getCommands() throws ProxyException {
		return (JsonObject)doGet("/game/commands");
	}

	@Override
	public JsonObject sendChat(SendChat sendchat) throws ProxyException {
		return (JsonObject)doPost("/moves/sendChat", sendchat);
	}

	@Override
	public JsonObject rollNumber(RollNumber rollnum) throws ProxyException {
		return (JsonObject)doPost("/moves/rollNumber", rollnum);
	}

	@Override
	public JsonObject robPlayer(RobPlayer robplayer) throws ProxyException {
		return (JsonObject)doPost("/moves/robPlayer", robplayer);
	}

	@Override
	public JsonObject finishTurn(FinishMove finishmove) throws ProxyException {
		return (JsonObject)doPost("/moves/finishTurn", finishmove);
	}

	@Override
	public JsonObject buyDevCard(BuyDevCard buydev) throws ProxyException {
		return (JsonObject)doPost("/moves/buyDevCard", buydev);
	}

	@Override
	public JsonObject Year_of_Plenty(Year_of_Plenty_ yop) throws ProxyException {
		return (JsonObject)doPost("/moves/Year_of_Plenty", yop);
	}

	@Override
	public JsonObject Road_Building(Road_Building_ roadbuild) throws ProxyException {
		return (JsonObject)doPost("/moves/Road_Building", roadbuild);
	}

	@Override
	public JsonObject Soldier(Soldier_ soldier) throws ProxyException {
		return (JsonObject)doPost("/moves/Soldier", soldier);
	}

	@Override
	public JsonObject Monopoly(Monopoly_ monopoly) throws ProxyException {
		return (JsonObject)doPost("/moves/Monopoly", monopoly);
	}

	@Override
	public JsonObject Monument(Monument_ monument) throws ProxyException {
		return (JsonObject)doPost("/moves/Monument", monument);
	}

	@Override
	public JsonObject buildRoad(BuildRoad buildroad) throws ProxyException {
		return (JsonObject)doPost("/moves/buildRoad", buildroad);
	}

	@Override
	public JsonObject buildSettlement(BuildSettlement buildsettlement) throws ProxyException {
		return (JsonObject)doPost("/moves/buildSettlement", buildsettlement);
	}

	@Override
	public JsonObject buildCity(BuildCity buildcity) throws ProxyException {
		return (JsonObject)doPost("/moves/buildCity", buildcity);
	}

	@Override
	public JsonObject offerTrade(OfferTrade tradeOffer) throws ProxyException {
		return (JsonObject)doPost("/moves/offerTrade", tradeOffer);
	}

	@Override
	public JsonObject acceptTrade(AcceptTrade tradeAccept) throws ProxyException {
		return (JsonObject)doPost("/moves/acceptTrade", tradeAccept);
	}

	@Override
	public JsonObject maritimeTrade(MaritimeTrade tradeMaritime) throws ProxyException {
		return (JsonObject)doPost("/moves/maritimeTrade", tradeMaritime);
	}

	@Override
	public JsonObject discardCards(DiscardCards discard) throws ProxyException {
		return (JsonObject)doPost("/moves/discardCards", discard);
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
