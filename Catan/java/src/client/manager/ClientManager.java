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
	private static ArrayList <User> players;
	private static ServerProxy serverProxy = new ServerProxy();
	private static ModelFacade modelFacade = new ModelFacade();
	private PlayerInfo currentPlayerInfo;
	private GameInfo currentGameInfo;
	
	private ClientManager() {
		currentPlayerInfo = new PlayerInfo();
	}
	
	public static ClientManager instance(){
		if (instance != null) {
			return instance;
		} else {
			return new ClientManager();
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
	
	public PlayerInfo getCurrentPlayerInfo(){
		return currentPlayerInfo;
	}
	
	public void setPlayerName(String name){
		currentPlayerInfo.setName(name);
	}
	
	public void setPlayerID(int id){
		currentPlayerInfo.setId(id);
	}
	
	public void setPlayerIndex(int index){
		currentPlayerInfo.setPlayerIndex(index);
	}
	
	public void setPlayerColor(CatanColor color){
		currentPlayerInfo.setColor(color);
	}

	public GameInfo getCurrentGameInfo() {
		return currentGameInfo;
	}

	public void setCurrentGameInfo(GameInfo currentGameInfo) {
		System.out.println("GameInfo being set in Client Manager");
		System.out.println("\tGame ID: " + currentGameInfo.getId());
		this.currentGameInfo = currentGameInfo;
	}
	

}
