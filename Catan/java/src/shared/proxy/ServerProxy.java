package shared.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;
import com.thoughtworks.xstream.io.json.JsonWriter;

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
	private Gson gson = new Gson();

	
	private XStream jsonStream;
	/** Default Constructor*/
	public ServerProxy(){
		jsonStream = new XStream(new JsonHierarchicalStreamDriver() {
		    public HierarchicalStreamWriter createWriter(Writer writer) {
		        return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
		    }
		});
	}
	
	public ServerProxy(String host, String port){
		jsonStream = new XStream(new JsonHierarchicalStreamDriver() {
		    public HierarchicalStreamWriter createWriter(Writer writer) {
		        return new JsonWriter(writer, JsonWriter.DROP_ROOT_MODE);
		    }
		});
		SERVER_HOST = host;
		SERVER_PORT = Integer.parseInt(port);
		URL_PREFIX = "http://" + SERVER_HOST + ":" + SERVER_PORT;
	}
	
	public JsonElement getJson(InputStream input) throws UnsupportedEncodingException{
		BufferedReader streamReader = new BufferedReader(new InputStreamReader(input, "UTF-8")); 
	    StringBuilder responseStrBuilder = new StringBuilder();

	    String inputStr;
	    try {
			while ((inputStr = streamReader.readLine()) != null)
			    responseStrBuilder.append(inputStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    String jsonstff = responseStrBuilder.toString();
		
		
		JsonElement element = gson.fromJson (jsonstff, JsonElement.class);
		return element;
	}
	
	private JsonElement doGet(String urlPath) throws ProxyException{
		try{
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_GET);
			connection.connect();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				 return getJson(connection.getInputStream());
			}
			else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST)
			{
				return null;
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
	
	private JsonElement doPost(String urlPath, Object postData) throws ProxyException{
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
				return getJson(connection.getInputStream());
			}
			else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST)
			{
				return null;
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
	
	private boolean doLogin(String urlPath, Object postData) throws ProxyException{
		try{
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.addRequestProperty("Accept", "text/html");
			connection.connect();
			String param = gson.toJson(postData);
			connection.getOutputStream().write(param.getBytes());
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				String cookieheader = connection.getHeaderField("Set-cookie");
				StringBuilder sb = new StringBuilder(cookieheader);
				sb.delete(0, 10);
				int length = sb.length();
				sb.delete(length-8, length-1);
				setUsercookie(sb.toString());
			   
			    return true;
			}
			else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST)
			{
				throw new ProxyException(connection.getResponseMessage());
			}
			else{
				throw new ProxyException(String.format("Request failed: (http code %d)",
						connection.getResponseCode()));
			}
		}
		catch (IOException e) {
			throw new ProxyException(String.format("Request failed: %s", e.getMessage()));
		}
	}
	
	private JsonElement doJoin(String urlPath, Object postData) throws ProxyException{
		try{
			URL url = new URL(URL_PREFIX + urlPath);
			HttpURLConnection connection = (HttpURLConnection)url.openConnection();
			connection.setRequestMethod(HTTP_POST);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.addRequestProperty("Accept", "text/html");
			connection.setRequestProperty("Cookie", usercookie);
			connection.connect();
			connection.getOutputStream().close();
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK){
				String cookieheader = connection.getHeaderField("Set-cookie");
				StringBuilder sb = new StringBuilder(cookieheader);
				sb.substring(11, 12);
				setGameID(sb.toString());
				return getJson(connection.getInputStream());
			}
			else if (connection.getResponseCode() == HttpURLConnection.HTTP_BAD_REQUEST)
			{
				return null;
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
	public boolean login(Credentials cred) throws ProxyException {
		return doLogin("/user/login", cred);
	}

	@Override
	public boolean register(Credentials cred) throws ProxyException {
		return doLogin("/user/register", cred);
	}

	@Override
	public JsonElement list() throws ProxyException {
		return doGet("/games/list");
	}

	@Override
	public JsonElement create(CreateGameRequest CreateRequest) throws ProxyException {
		return doPost("/games/create", CreateRequest);
	}

	@Override
	public JsonElement join(JoinGameRequest JoinRequest) throws ProxyException {
		return doJoin("/games/join", JoinRequest);
		
	}

	@Override
	public JsonElement save(SaveGameRequest SaveRequest) throws ProxyException {
		return doPost("/games/save", SaveRequest);
	}

	@Override
	public JsonElement load(LoadGameRequest LoadRequest) throws ProxyException {
		return doPost("/games/load", LoadRequest);
		
	}

	@Override
	public JsonElement model(int version) throws ProxyException {
		return doGet("/game/model");
	}

	@Override
	public JsonElement reset() throws ProxyException {
		return doGet("/game/reset");
	}

	@Override
	public JsonElement postCommands(List<String> commandslist) throws ProxyException {
		return doPost("/game/commands", commandslist);
	}

	@Override
	public JsonElement getCommands() throws ProxyException {
		return doGet("/game/commands");
	}

	@Override
	public JsonElement sendChat(SendChat sendchat) throws ProxyException {
		return doPost("/moves/sendChat", sendchat);
	}

	@Override
	public JsonElement rollNumber(RollNumber rollnum) throws ProxyException {
		return doPost("/moves/rollNumber", rollnum);
	}

	@Override
	public JsonElement robPlayer(RobPlayer robplayer) throws ProxyException {
		return doPost("/moves/robPlayer", robplayer);
	}

	@Override
	public JsonElement finishTurn(FinishMove finishmove) throws ProxyException {
		return doPost("/moves/finishTurn", finishmove);
	}

	@Override
	public JsonElement buyDevCard(BuyDevCard buydev) throws ProxyException {
		return doPost("/moves/buyDevCard", buydev);
	}

	@Override
	public JsonElement Year_of_Plenty(Year_of_Plenty_ yop) throws ProxyException {
		return doPost("/moves/Year_of_Plenty", yop);
	}

	@Override
	public JsonElement Road_Building(Road_Building_ roadbuild) throws ProxyException {
		return doPost("/moves/Road_Building", roadbuild);
	}

	@Override
	public JsonElement Soldier(Soldier_ soldier) throws ProxyException {
		return doPost("/moves/Soldier", soldier);
	}

	@Override
	public JsonElement Monopoly(Monopoly_ monopoly) throws ProxyException {
		return doPost("/moves/Monopoly", monopoly);
	}

	@Override
	public JsonElement Monument(Monument_ monument) throws ProxyException {
		return doPost("/moves/Monument", monument);
	}

	@Override
	public JsonElement buildRoad(BuildRoad buildroad) throws ProxyException {
		return doPost("/moves/buildRoad", buildroad);
	}

	@Override
	public JsonElement buildSettlement(BuildSettlement buildsettlement) throws ProxyException {
		return doPost("/moves/buildSettlement", buildsettlement);
	}

	@Override
	public JsonElement buildCity(BuildCity buildcity) throws ProxyException {
		return doPost("/moves/buildCity", buildcity);
	}

	@Override
	public JsonElement offerTrade(OfferTrade tradeOffer) throws ProxyException {
		return doPost("/moves/offerTrade", tradeOffer);
	}

	@Override
	public JsonElement acceptTrade(AcceptTrade tradeAccept) throws ProxyException {
		return doPost("/moves/acceptTrade", tradeAccept);
	}

	@Override
	public JsonElement maritimeTrade(MaritimeTrade tradeMaritime) throws ProxyException {
		return doPost("/moves/maritimeTrade", tradeMaritime);
	}

	@Override
	public JsonElement discardCards(DiscardCards discard) throws ProxyException {
		return doPost("/moves/discardCards", discard);
	}

	@Override
	public JsonElement changeLogLevel(ChangeLogLevelRequest cllr) throws ProxyException {
		return doPost("/util/changeLogLevel", cllr);
	}

	@Override
	public JsonElement addAI(AddAIRequest addAIreq) throws ProxyException {
		return doPost("/game/addAI", addAIreq);
		
	}

	@Override
	public JsonElement listAI() throws ProxyException {
		return doGet("/game/listAI");
	}

}
