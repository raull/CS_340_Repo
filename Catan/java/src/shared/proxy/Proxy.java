package shared.proxy;

import java.util.List;

import com.google.gson.JsonObject;

import shared.model.*;
import shared.proxy.game.*;
import shared.proxy.games.*;
import shared.proxy.moves.*;
import shared.proxy.user.*;
import shared.proxy.util.*;
/**
 * Interface used by the Client to connect to the Server and perform
 * necessary functions upon it.
 * @author Kent
 *
 */
public interface Proxy {
	
	/**
	 * Takes a User's credentials to log them into the server
	 * @param cred
	 */
	public void login(Credentials cred) throws ProxyException;
	/**
	 * Takes a User's credentials to register them on the server.
	 * @param cred
	 */
	public void register(Credentials cred)throws ProxyException;
	/**
	 * Returns a list of all games in progress and their players.
	 * @return
	 */
	public List<Game> list()throws ProxyException;
	/**
	 * Requests that a new game be created.
	 * @param CreateRequest
	 * @return
	 */
	public  NewGame create(CreateGameRequest CreateRequest)throws ProxyException;
	/**
	 * Requests to join a particular game, with a specific color for the player
	 * @param JoinRequest
	 */
	public void join (JoinGameRequest JoinRequest)throws ProxyException;
	/**
	 * Requests that the current game state be saved to a file
	 * @param SaveRequest
	 */
	public void save(SaveGameRequest SaveRequest)throws ProxyException;
	/**
	 * Requests that a game be loaded from a saved file
	 * @param LoadRequest
	 */
	public void load(LoadGameRequest LoadRequest)throws ProxyException;
	/**
	 * Returns the current state of the game in JSON format, if the client's version
	 * is out of date.
	 * @param version
	 * @return
	 */
	public JsonObject model(int version)throws ProxyException;
	/**
	 * Clears out the command history of the current game, resetting it to the beginning
	 * for user created games, and to just after the initial placement round for 
	 * server created games.
	 * @return
	 */
	public JsonObject reset()throws ProxyException; 
	/**
	 * Executes the list of given commands
	 * @param commandslist
	 * @return
	 */
	public JsonObject postCommands(List<String> commandslist)throws ProxyException;
	/**
	 * Returns a list of all the commands that have been given in this game.
	 * @return
	 */
	public JsonObject getCommands()throws ProxyException;

	/**
	 * Sends a chat message coupled with the sender to the server
	 * @param sendchat
	 * @return
	 */
	public JsonObject sendChat(SendChat sendchat)throws ProxyException;
	/**
	 * Lets the server know that a specific player has rolled a specific number.
	 * @param rollnum
	 * @return
	 */
	public JsonObject rollNumber(RollNumber rollnum)throws ProxyException;
	/**
	 * Robs a player and moves the robber
	 * @param robplayer
	 * @return
	 */
	public JsonObject robPlayer(RobPlayer robplayer)throws ProxyException;
	/**
	 * Ends a player's turn
	 * @param finishmove
	 * @return
	 */
	public JsonObject finishTurn(FinishMove finishmove)throws ProxyException;
	/**
	 * Used to buy a development card	
	 * @param buydev
	 * @return
	 */
	public JsonObject buyDevCard(BuyDevCard buydev)throws ProxyException;
	/**
	 * Plays a "Year of Plenty" card from the hand 
	 * @param yop
	 * @return
	 */
	public JsonObject Year_of_Plenty(Year_of_Plenty_ yop)throws ProxyException;
	/**
	 * Plays a "Road Building" card from the hand
	 * @param roadbuild
	 * @return
	 */
	public JsonObject Road_Building(Road_Building_ roadbuild)throws ProxyException;
	/**
	 * Plays a "Soldier" card from your hand, selecting a new robber position
	 * and a player to rob.
	 * @param soldier
	 * @return
	 */
	public JsonObject Soldier(Soldier_ soldier)throws ProxyException;
	/**
	 * Plays a "Monopoly" card from your hand to monopolize the specified
	 * resource.
	 * @param monopoly
	 * @return
	 */

	public JsonObject Monopoly(Monopoly_ monopoly)throws ProxyException;
	/**
	 * Plays a "Monument" card from your hand to give you a victory point.
	 * @param monument
	 * @return
	 */
	public JsonObject Monument(Monument_ monument)throws ProxyException;
	/**
	 * Builds a road at a specified location.
	 * @param buildroad
	 * @return
	 */
	public JsonObject buildRoad(BuildRoad buildroad)throws ProxyException;
	/**
	 * Builds a settlement at the specified location.
	 * @param buildsettlement
	 * @return
	 */
	public JsonObject buildSettlement(BuildSettlement buildsettlement)throws ProxyException;
	/**
	 * Builds a city at the specified location.
	 * @param buildcity
	 * @return
	 */
	public JsonObject buildCity(BuildCity buildcity)throws ProxyException;
	/**
	 * Offers a domestic trade to another player
	 * @param tradeOffer
	 * @return
	 */
	public JsonObject offerTrade(OfferTrade tradeOffer)throws ProxyException;
	/**
	 * Used to accept or decline a trade offered to you
	 * @param tradeAccept
	 * @return
	 */
	public JsonObject acceptTrade(AcceptTrade tradeAccept)throws ProxyException;
	/**
	 * Executes a Maritime trade
	 * @param tradeMaritime
	 * @return
	 */
	public JsonObject maritimeTrade(MaritimeTrade tradeMaritime)throws ProxyException;
	/**
	 * Discards the specified resource cards
	 * @param discard
	 * @return
	 */
	public JsonObject discardCards(DiscardCards discard)throws ProxyException;
	/**
	 * Set's the server's log level.
	 * @param cllr
	 */
	public void changeLogLevel(ChangeLogLevelRequest cllr)throws ProxyException;
	/**
	 * Adds an AI player to the current game
	 * @param addAIreq
	 */
	public void addAI(AddAIRequest addAIreq)throws ProxyException;
	/**
	 * Returns a list of the supported AI types.
	 * @return
	 */
	public List<String> listAI()throws ProxyException;
}



