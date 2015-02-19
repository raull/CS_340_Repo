package client.manager;

import java.util.ArrayList;

import client.data.GameInfo;
import client.data.PlayerInfo;
import shared.definitions.CatanColor;
import shared.model.facade.ModelFacade;
import shared.model.game.User;
import shared.proxy.ServerProxy;


/**
 * Client Manager is a singleton class that handles the {@link ModelFacade} and {@link ServerProxy}
 * for easier access. This class alse serves to store values for easy access as well, for example the user ID that the client is currently logged in as
 * 
 * @author thyer
 *
 */
public class ClientManager {
	
	private static ClientManager instance;
	private static ServerProxy serverProxy = new ServerProxy();
	private static ModelFacade modelFacade = new ModelFacade();
	private static PlayerInfo currentPlayerInfo = new PlayerInfo();
	private static GameInfo currentGameInfo = new GameInfo();
	
	private ClientManager() {
	}
	
	public static ClientManager instance(){
		if (instance != null) {
			return instance;
		} else {
			instance = new ClientManager();
			return instance;
		}
	}
	
	/**
	 * Get the client Model Facade
	 * @return The singleton copy of the Model Facade
	 */
	public ModelFacade getModelFacade() {
		return modelFacade;
	}
	
	/**
	 * Get the client Server Proxy
	 * @return The singleton copy of the Server Proxy
	 */
	public ServerProxy getServerProxy() {
		return serverProxy;
	}
	
	public static PlayerInfo getCurrentPlayerInfo(){
		return currentPlayerInfo;
	}
	
	public static void setPlayerName(String name){
		currentPlayerInfo.setName(name);
	}
	
	public static void setPlayerID(int id){
		currentPlayerInfo.setId(id);
	}
	
	public static void setPlayerIndex(int index){
		currentPlayerInfo.setPlayerIndex(index);
	}
	
	public static void setPlayerColor(CatanColor color){
		currentPlayerInfo.setColor(color);
	}

	public static GameInfo getCurrentGameInfo() {
		return currentGameInfo;
	}

	public static void setCurrentGameInfo(GameInfo gi) {
		currentGameInfo.setId(gi.getId());
		currentGameInfo.setTitle(gi.getTitle());
		for(PlayerInfo pi : gi.getPlayers()){
			currentGameInfo.addPlayer(pi);
		}
	}
	

}
