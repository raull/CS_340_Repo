package shared.proxy;

import java.util.List;

import shared.model.*;


import shared.model.Model;

import shared.proxy.communication.*;
/**
 * Used by the Client to connect to the Server and perform
 * necessary functions upon it.
 * @author Kent
 *
 */
public interface Proxy {
	
	/**
	 * Takes a User's credentials to log them into the server
	 * @param cred
	 */
	public void login(Credentials cred);
	/**
	 * Takes a User's credentials to register them on the server.
	 * @param cred
	 */
	public void register(Credentials cred);
	/**
	 * Returns a list of all games in progress and their players.
	 * @return
	 */
	public List<Game> list();
	/**
	 * Requests that a new game be created.
	 * @param CreateRequest
	 * @return
	 */
	public  NewGame create(CreateGameRequest CreateRequest);
	/**
	 * Requests to join a particular game, with a specific color for the player
	 * @param JoinRequest
	 */
	public void join (JoinGameRequest JoinRequest);
	/**
	 * Requests that the current game state be saved to a file
	 * @param SaveRequest
	 */
	public void save(SaveGameRequest SaveRequest);
	/**
	 * Requests that a game be loaded from a saved file
	 * @param LoadRequest
	 */
	public void load(LoadGameRequest LoadRequest);
	/**
	 * Returns the current state of the game in JSON format, if the client's version
	 * is out of date.
	 * @param version
	 * @return
	 */
	public Model model(int version);
	/**
	 * Clears out the command history of the current game, resetting it to the beginning
	 * for user created games, and to just after the initial placement round for 
	 * server created games.
	 * @return
	 */
	public Model reset(); 
	/**
	 * Executes the list of given commands
	 * @param commandslist
	 * @return
	 */
	public Model postCommands(List<String> commandslist);
	/**
	 * Returns a list of all the commands that have been given in this game.
	 * @return
	 */
	public List<String> getCommands();

	/**
	 * Sends a chat message coupled with the sender to the server
	 * @param sendchat
	 * @return
	 */
	public Model sendChat(SendChat sendchat);
	/**
	 * Lets the server know that a specific player has rolled a specific number.
	 * @param rollnum
	 * @return
	 */
	public Model rollNumber(RollNumber rollnum);
	/**
	 * Robs a player and moves the robber
	 * @param robplayer
	 * @return
	 */
	public Model robPlayer(RobPlayer robplayer);
	/**
	 * Ends a player's turn
	 * @param finishmove
	 * @return
	 */
	public Model finishTurn(FinishMove finishmove);
	/**
	 * Used to buy a development card	
	 * @param buydev
	 * @return
	 */
	public Model buyDevCard(BuyDevCard buydev);
	/**
	 * Plays a "Year of Plenty" card from the hand 
	 * @param yop
	 * @return
	 */
	public Model Year_of_Plenty(Year_of_Plenty_ yop);
	/**
	 * Plays a "Road Building" card from the hand
	 * @param roadbuild
	 * @return
	 */
	public Model Road_Building(Road_Building_ roadbuild);
	/**
	 * Plays a "Soldier" card from your hand, selecting a new robber position
	 * and a player to rob.
	 * @param soldier
	 * @return
	 */
	public Model Soldier(Soldier_ soldier);
	/**
	 * Plays a "Monopoly" card from your hand to monopolize the specified
	 * resource.
	 * @param monopoly
	 * @return
	 */

	public Model Monopoly(Monopoly_ monopoly);
	/**
	 * Plays a "Monument" card from your hand to give you a victory point.
	 * @param monument
	 * @return
	 */
	public Model Monument(Monument_ monument);
	/**
	 * Builds a road at a specified location.
	 * @param buildroad
	 * @return
	 */
	public Model buildRoad(BuildRoad buildroad);
	/**
	 * Builds a settlement at the specified location.
	 * @param buildsettlement
	 * @return
	 */
	public Model buildSettlement(BuildSettlement buildsettlement);
	/**
	 * Builds a city at the specified location.
	 * @param buildcity
	 * @return
	 */
	public Model buildCity(BuildCity buildcity);
	/**
	 * Offers a domestic trade to another player
	 * @param tradeOffer
	 * @return
	 */
	public Model offerTrade(OfferTrade tradeOffer);
	/**
	 * Used to accept or decline a trade offered to you
	 * @param tradeAccept
	 * @return
	 */
	public Model acceptTrade(AcceptTrade tradeAccept);
	/**
	 * Executes a Maritime trade
	 * @param tradeMaritime
	 * @return
	 */
	public Model maritimeTrade(MaritimeTrade tradeMaritime);
	/**
	 * Discards the specified resource cards
	 * @param discard
	 * @return
	 */
	public Model discardCards(DiscardCards discard);
}



