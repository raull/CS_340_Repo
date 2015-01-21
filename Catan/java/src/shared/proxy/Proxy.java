package shared.proxy;

import java.util.List;

import shared.proxy.communication.*;
/**
 * Used by the Client to connect to the Server and perform
 * necessary functions upon it.
 * @author Kent
 *
 */
public class Proxy {
	
	/**
	 * Takes a User's credentials to log them into the server
	 * @param cred
	 */
	public void login(Credentials cred){
		//CODE
	}
	/**
	 * Takes a User's credentials to register them on the server.
	 * @param cred
	 */
	public void register(Credentials cred){
		//CODE
	}
	/**
	 * Returns a list of all games in progress and their players.
	 * @return
	 */
	public List<Game> list(){
		return null;
		// CODE
	}
	/**
	 * Requests that a new game be created.
	 * @param CreateRequest
	 * @return
	 */
	public  NewGame create(CreateGameRequest CreateRequest){
		return null;
		//CODE
	}
	/**
	 * Requests to join a particular game, with a specific color for the player
	 * @param JoinRequest
	 */
	public void join (JoinGameRequest JoinRequest){
		//CODE
	}
	/**
	 * Requests that the current game state be saved to a file
	 * @param SaveRequest
	 */
	public void save(SaveGameRequest SaveRequest){
		//CODE
	}
	/**
	 * Requests that a game be loaded from a saved file
	 * @param LoadRequest
	 */
	public void load(LoadGameRequest LoadRequest){
		//CODE
	}
<<<<<<< HEAD
	/**
	 * Sends a chat message coupled with the sender to the server
	 * @param sendchat
	 * @return
	 */
	public Model sendChat(SendChat sendchat){
		return null;
	}
	/**
	 * Lets the server know that a specific player has rolled a specific number.
	 * @param rollnum
	 * @return
	 */
	public Model rollNumber(RollNumber rollnum){
		return null;
	}
	/**
	 * Robs a player and moves the robber
	 * @param robplayer
	 * @return
	 */
	public Model robPlayer(RobPlayer robplayer){
		return null;
	}
	/**
	 * Ends a player's turn
	 * @param finishmove
	 * @return
	 */
	public Model finishTurn(FinishMove finishmove){
		return null;
	}
	/**
	 * Used to buy a development card	
	 * @param buydev
	 * @return
	 */
	public Model buyDevCard(BuyDevCard buydev){
		return null;
	}
	/**
	 * Plays a "Year of Plenty" card from the hand 
	 * @param yop
	 * @return
	 */
	public Model Year_of_Plenty(Year_of_Plenty_ yop){
		return null;
	}
	/**
	 * Plays a "Road Building" card from the hand
	 * @param roadbuild
	 * @return
	 */
	public Model Road_Building(Road_Building_ roadbuild){
		return null;
	}
	/**
	 * Plays a "Soldier" card from your hand, selecting a new robber position
	 * and a player to rob.
	 * @param soldier
	 * @return
	 */
	public Model Soldier(Soldier_ soldier){
		return null;
	}
	/**
	 * Plays a "Monopoly" card from your hand to monopolize the specified
	 * resource.
	 * @param monopoly
	 * @return
	 */
	public Model Monopoly(Monopoly_ monopoly){
		return null;
	}
=======
	
>>>>>>> origin/master
}
